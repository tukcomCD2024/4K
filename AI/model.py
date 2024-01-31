import pandas as pd
import numpy as np
import os
import json

# 백슬래쉬(\)가 유닡코드 이스케이프로 해석되려고 하기 때문에 문제가 발생할 수 있음
# 때문에 문자열 앞에 'r'을 추가하여 이스케이프 문자를 무시하도록 함
TL_sentence_path = r'C:\Users\edcrf\sentence_dataTL.csv'
VL_sentence_path = r'C:\Users\edcrf\sentence_dataVL.csv'
TL_word_path = r'C:\Users\edcrf\word_dataTL.csv'
VL_word_path = r'C:\Users\edcrf\word_dataVL.csv'
standard_word_path = r'C:\Users\edcrf\all_standard_data.csv'

# data파일 불러오기
TL_sentence_data = pd.read_csv(TL_sentence_path, encoding='utf-8')
VL_sentence_data = pd.read_csv(VL_sentence_path, encoding='utf-8')
TL_word_data = pd.read_csv(TL_word_path, encoding='utf-8')
VL_word_data = pd.read_csv(VL_word_path, encoding='utf-8')
standard_word_data = pd.read_csv(standard_word_path, encoding='utf-8')

# 중복 제거, Pronuncication 열은 필요 없다고 생각
TL_sentence_data.drop('Pronunciation', axis=1, inplace=True)
TL_sentence_data = TL_sentence_data.drop_duplicates().reset_index(drop=True)
VL_sentence_data.drop('Pronunciation', axis=1, inplace=True)
VL_sentence_data = VL_sentence_data.drop_duplicates().reset_index(drop=True)
TL_word_data.drop('Pronunciation', axis=1, inplace=True)
TL_word_data = TL_word_data.drop_duplicates().reset_index(drop=True)
VL_word_data.drop('Pronunciation', axis=1, inplace=True)
VL_word_data = VL_word_data.drop_duplicates().reset_index(drop=True)



### 1. 방언-표준어 단어 사용하지 않고 방언-표준어 문장 각각 형태소 분석
# mecab = Mecab()
# preprocessed_dialect = []   # 방언문장 형태소 저장
# preprocessed_standard = []  # 표준어문장 형태소 저장
# for i in TL_sentence_data['Dialect']:
#     tokenized_dialect = mecab.morphs(i)
#     result_dialect = []

#     for token in tokenized_dialect:
#         result_dialect.append(token)
#     preprocessed_dialect.append(result_dialect)

# for i in TL_sentence_data['Standard']:
#     tokenized_standard = mecab.morphs(i)
#     result_standard = []

#     for token in tokenized_standard:
#         result_standard.append(token)
#     preprocessed_standard.append(result_standard)

# # 방언과 표준어 문장의 토큰 수 비교
# standard_token = 0
# dialect_token = 0
# for i in range(0, len(TL_sentence_data)):
#     standard_token += len(preprocessed_standard[i])
#     dialect_token += len(preprocessed_dialect[i])
# print(f"표준어 토큰의 수: {standard_token}")
# print(f"방언 토큰의 수: {dialect_token}")
#     # 결과 => 표준어 토큰 수: 9181798, 방언 토큰 수: 9409394

# # -> 다른 방식을 생각



### 2. 방언-표준어 단어를 사용해 단어 사전 생성
# word_set = set()

# # 표준어와 단어 리스트 분리
# standard_words = TL_word_data['Standard']
# dialect_words = TL_word_data['Dialect']

# # 단어 사전에 추가
# word_set.update(standard_words)
# word_set.update(dialect_words)

# # word_set 리스트로 변환하여 저장
# word_dict = list(word_set)

# # 각 단어에 대한 인덱스 매핑해 딕셔너리에 저장
# word_to_index = {word: idx for idx, word in enumerate(word_dict)}



### 3. 방언-표준어 단어 묶음을 사용하고, 변경이 일어나지 않은 표준어도 사용
Dialect = TL_word_data['Dialect']
Standard = TL_word_data['Standard']

dictionary = pd.DataFrame([Dialect, Standard])
dictionary = dictionary.transpose()
dictionary.columns = ['Dialect', 'Standard']
Dialect_dictionary = dictionary['Dialect']
Standard_dictionary = dictionary['Standard']

id_to_dialect = Dialect_dictionary.to_dict()
id_to_standard = Standard_dictionary.to_dict()

dialect_to_id = {v:k for k, v in id_to_dialect.items()}
standard_to_id = {v:k for k, v in id_to_standard.items()}

save_path = r'C:\Users\edcrf'

with open(os.path.join(save_path, 'id_to_dialect.json'), 'w') as f:
    json.dump(id_to_dialect, f)

with open(os.path.join(save_path, 'id_to_standard.json'), 'w') as f:
    json.dump(id_to_standard, f)

with open(os.path.join(save_path, 'dialect_to_id.json'), 'w') as f:
    json.dump(dialect_to_id, f)

with open(os.path.join(save_path, 'standard_to_id.json'), 'w') as f:
    json.dump(standard_to_id, f)

# standard_word_data에는 변환이 일어나지 않은 단어 포함되어 있음