import re
import konlpy
import pandas as pd
from konlpy.tag import Okt
from keras.preprocessing.sequence import pad_sequences

max_length = 41

def preprocess_input(text):
    okt = Okt()
    word_to_id = pd.read_csv("offer_service/word_to_id_all.csv", index_col=0)

    input_tokens = []
    tokens = okt.morphs(text)

    for token in tokens:
        if token in word_to_id.index:  # word_to_id에 해당 토큰이 존재하는지 확인
            input_tokens.append(word_to_id.loc[token].values[0])  # 해당 단어의 정수값을 input_tokens에 추가
        else:
            input_tokens.append(word_to_id.loc['<UNK>'].values[0])  # 사전에 없는 단어는 <UNK>정수값을 추가

    padding_input_tokens = pad_sequences([input_tokens], maxlen=max_length, padding='post', truncating='post')

    return padding_input_tokens