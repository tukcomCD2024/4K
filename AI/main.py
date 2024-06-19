import torch
from fastapi import FastAPI
from pydantic import BaseModel
import torch.nn as nn
import json

class Lang:
    def __init__(self, name):
        self.name = name
        self.word2index = {"UNK": 2}
        self.word2count = {}
        self.index2word = {0: "SOS", 1: "EOS", 2: "UNK", 3: "PAD"}
        self.n_words = 4  # SOS, EOS, UNK, PAD

    def addSentence(self, sentence):
        for word in sentence.split(' '):
            self.addWord(word)

    def addWord(self, word):
        if word not in self.word2index:
            self.word2index[word] = self.n_words
            self.word2count[word] = 1
            self.index2word[self.n_words] = word
            self.n_words += 1
        else:
            self.word2count[word] += 1

    def getWordIndex(self, word):
        return self.word2index.get(word, self.word2index["UNK"])

    def saveLang(self, filename):
        with open(filename, 'w', encoding='utf-8') as f:
            json.dump({
                'word2index': self.word2index,
                'index2word': self.index2word,
                'n_words': self.n_words
            }, f, ensure_ascii=False, indent=4)

    def loadLang(self, filename):
        with open(filename, 'r', encoding='utf-8') as f:
            data = json.load(f)
            self.word2index = data['word2index']
            self.index2word = data['index2word']
            self.n_words = data['n_words']

def indexesFromSentence(lang, sentence):
    return [lang.getWordIndex(word) for word in sentence.split(' ')]

def tensorFromSentence(lang, sentence, max_length):
    indexes = indexesFromSentence(lang, sentence)
    indexes.append(EOS_token)
    if len(indexes) < max_length:
        indexes += [PAD_token] * (max_length - len(indexes))
    elif len(indexes) > max_length:
        indexes = indexes[:max_length-1] + [EOS_token]
    return torch.tensor(indexes, dtype=torch.long).view(-1, 1)

class EncoderRNN(nn.Module):
    def __init__(self, input_size, hidden_size):
        super(EncoderRNN, self).__init__()
        self.hidden_size = hidden_size
        self.embedding = nn.Embedding(input_size, hidden_size)
        self.lstm = nn.LSTM(hidden_size, hidden_size)

    def forward(self, input, hidden):
        embedded = self.embedding(input).view(1, 1, -1)
        output, hidden = self.lstm(embedded, hidden)
        return output, hidden

    def initHidden(self):
        return (torch.zeros(1, 1, self.hidden_size),
                torch.zeros(1, 1, self.hidden_size))

class AttnDecoderRNN(nn.Module):
    def __init__(self, hidden_size, output_size, dropout_p=0.1, max_length=22):
        super(AttnDecoderRNN, self).__init__()
        self.hidden_size = hidden_size
        self.output_size = output_size
        self.dropout_p = dropout_p
        self.max_length = max_length

        self.embedding = nn.Embedding(self.output_size, self.hidden_size)
        self.attn = nn.Linear(self.hidden_size * 2, self.max_length)
        self.attn_combine = nn.Linear(self.hidden_size * 2, self.hidden_size)
        self.dropout = nn.Dropout(self.dropout_p)
        self.lstm = nn.LSTM(self.hidden_size, self.hidden_size)
        self.out = nn.Linear(self.hidden_size, self.output_size)

    def forward(self, input, hidden, encoder_outputs):
        embedded = self.embedding(input).view(1, 1, -1)
        embedded = self.dropout(embedded)

        attn_weights = nn.functional.softmax(
            self.attn(torch.cat((embedded[0], hidden[0][0]), 1)), dim=1)
        attn_applied = torch.bmm(attn_weights.unsqueeze(0),
                                 encoder_outputs.unsqueeze(0))

        output = torch.cat((embedded[0], attn_applied[0]), 1)
        output = self.attn_combine(output).unsqueeze(0)

        output = nn.functional.relu(output)
        output, hidden = self.lstm(output, hidden)

        output = nn.functional.log_softmax(self.out(output[0]), dim=1)
        return output, hidden, attn_weights

    def initHidden(self):
        return (torch.zeros(1, 1, self.hidden_size),
                torch.zeros(1, 1, self.hidden_size))

# 모델 경로와 언어 사전
encoder_path = 'model_encoder.pth'
decoder_path = 'model_decoder.pth'
dialect_lang_path = 'dialect_lang.json'
standard_lang_path = 'standard_lang.json'
hidden_size = 256
max_len = 22  # 최대 문장 길이
SOS_token = 0
EOS_token = 1
UNK_token = 2
PAD_token = 3

# Lang 객체 생성
dialect_lang = Lang("Dialect")
standard_lang = Lang("Standard")
dialect_lang.loadLang(dialect_lang_path)
standard_lang.loadLang(standard_lang_path)

# 모델 로드 함수
def loadModel(encoder_path='encoder.pth', decoder_path='decoder.pth'):
    encoder = EncoderRNN(dialect_lang.n_words, hidden_size)
    decoder = AttnDecoderRNN(hidden_size, standard_lang.n_words, dropout_p=0.1)
    encoder.load_state_dict(torch.load(encoder_path))
    decoder.load_state_dict(torch.load(decoder_path))
    return encoder, decoder

encoder, decoder = loadModel(encoder_path, decoder_path)

# 예측 함수 정의
def predict(sentence):
    with torch.no_grad():
        input_tensor = tensorFromSentence(dialect_lang, sentence, max_len)
        input_length = input_tensor.size()[0]
        encoder_hidden = encoder.initHidden()

        encoder_outputs = torch.zeros(max_len, encoder.hidden_size)

        for ei in range(input_length):
            encoder_output, encoder_hidden = encoder(input_tensor[ei], encoder_hidden)
            encoder_outputs[ei] = encoder_output[0, 0]

        decoder_input = torch.tensor([[SOS_token]])
        decoder_hidden = encoder_hidden

        decoded_words = []

        for di in range(max_len):
            decoder_output, decoder_hidden, decoder_attention = decoder(
                decoder_input, decoder_hidden, encoder_outputs)
            topv, topi = decoder_output.topk(1)
            if topi.item() == EOS_token:
                decoded_words.append('<EOS>')
                break
            else:
                decoded_words.append(standard_lang.index2word[topi.item()])

            decoder_input = topi.squeeze().detach()

        return ' '.join(decoded_words)

# FastAPI 애플리케이션 생성
app = FastAPI()

# 입력 데이터 모델 정의
class SentenceRequest(BaseModel):
    sentence: str

# 엔드포인트 정의
@app.post("/predict/")
async def get_prediction(request: SentenceRequest):
    prediction = predict(request.sentence)
    return {"input": request.sentence, "prediction": prediction}

# 메인 실행 함수
if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000)