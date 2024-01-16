import pandas as pd
import os
import json

train_folder_path = [
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 1인 TR\TL_02. 경상도_01. 1인발화 따라말하기',
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 질문에 답 TR\TL_02. 경상도_02. 1인발화 질문에답하기',
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 2인 TR\TL_02. 경상도_03. 2인발화'    
]

validation_folder_path = [
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 1인 VL\VL_02. 경상도_01. 1인발화 따라말하기',
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 질문에 답 VL\VL_02. 경상도_02. 1인발화 질문에답하기',
    r'C:\Users\edcrf\OneDrive\바탕 화면\경상도 2인 VL\VL_02. 경상도_03. 2인발화'    
]


## train data에서 필요한 변수 뽑음

train_file_content = []

for path  in train_folder_path:
    # print(f'현재 작업 중인 경로: {path}')
    
    train_file_list = os.listdir(path)

    for file_name in train_file_list:
        train_file_path = os.path.join(path, file_name)

        try:
            with open(train_file_path, "r", encoding="utf-8") as file:
                content = file.read()
                json_content = json.loads(content)
                train_file_content.append(json_content)
        except json.JSONDecodeError as e:
            print(f"Error decoding JSON in file {train_file_path}: {e}")

train_dialect_list = []

for i in range(0, len(train_file_content)):
    train_dialect_list.append(train_file_content[i]['transcription']['segments'])

train_result_segments = []

for dialect_segment in train_dialect_list:
  for segment in dialect_segment:
    try:
        if 'standard' in segment and segment['standard'] and segment['dialect'] and segment['standard'] != segment['dialect']:
            train_result_segments.append(segment)
    except KeyError:
        print(f"'standard' key not found in segment of file {train_file_path}")

TL_dia = []
TL_pro = []
TL_sta = []

for segment in train_result_segments:
  TL_dia.append(segment.get('dialect', ''))
  TL_pro.append(segment.get('pronunciation', ''))
  TL_sta.append(segment.get('standard', ''))
  
train_sentence_list = []

for i in range(0, len(train_file_content)):
    train_sentence_list.extend(train_file_content[i]['transcription']['sentences'])
# extend 함수는 리스트에 다른 리스트의 모든 요소를 추가
# 이렇게 하면 sentence_list는 더 이상 리스트의 리스트가 아니라 하나의 리스트로 구성

TL_di = []
TL_pr = []
TL_st = []

for sentence_segment in train_sentence_list:
    TL_di.append(sentence_segment.get('dialect', ''))
    TL_pr.append(sentence_segment.get('pronunciation', ''))
    TL_st.append(sentence_segment.get('standard', ''))

## data 전처리해서 방언-표준어 문장과 단어들을 csv 파일로 저장

# 단어
TL_word_data = {'Dialect': TL_dia, 'Pronunciation': TL_pro, 'Standard': TL_sta}
TL_word_df = pd.DataFrame(TL_word_data)

# 문장
TL_sentence_data = {'Dialect': TL_di, 'Pronunciation': TL_pr, 'Standard': TL_st}
TL_sentence_df = pd.DataFrame(TL_sentence_data)

# csv 파일 저장
TL_word_df.to_csv(r'C:\Users\edcrf\word_dataTL.csv', index=False)
TL_sentence_df.to_csv(r'C:\Users\edcrf\sentence_dataTL.csv', index=False)


## validation data에서 필요한 변수 뽑음

validation_file_content = []

for path  in validation_folder_path:
    # print(f'현재 작업 중인 경로: {path}')
    
    validation_file_list = os.listdir(path)

    for file_name in validation_file_list:
        validation_file_path = os.path.join(path, file_name)

        try:
            with open(validation_file_path, "r", encoding="utf-8") as file:
                content = file.read()
                json_content = json.loads(content)
                validation_file_content.append(json_content)
        except json.JSONDecodeError as e:
            print(f"Error decoding JSON in file {validation_file_path}: {e}")

validation_dialect_list = []

for i in range(0, len(validation_file_content)):
    validation_dialect_list.append(validation_file_content[i]['transcription']['segments'])

validation_result_segments = []

for dialect_segment in validation_dialect_list:
  for segment in dialect_segment:
    try:
        if 'standard' in segment and segment['standard'] and segment['dialect'] and segment['standard'] != segment['dialect']:
            validation_result_segments.append(segment)
    except KeyError:
        print(f"'standard' key not found in segment of file {validation_file_path}")

VL_dia = []
VL_pro = []
VL_sta = []

for segment in validation_result_segments:
  VL_dia.append(segment.get('dialect', ''))
  VL_pro.append(segment.get('pronunciation', ''))
  VL_sta.append(segment.get('standard', ''))
  
VL_sentence_list = []

for i in range(0, len(validation_file_content)):
    VL_sentence_list.extend(validation_file_content[i]['transcription']['sentences'])
# extend 함수는 리스트에 다른 리스트의 모든 요소를 추가
# 이렇게 하면 sentence_list는 더 이상 리스트의 리스트가 아니라 하나의 리스트로 구성

VL_di = []
VL_pr = []
VL_st = []

for sentence_segment in VL_sentence_list:
    VL_di.append(sentence_segment.get('dialect', ''))
    VL_pr.append(sentence_segment.get('pronunciation', ''))
    VL_st.append(sentence_segment.get('standard', ''))

## data 전처리해서 방언-표준어 문장과 단어들을 csv 파일로 저장

# 단어
VL_word_data = {'Dialect': VL_dia, 'Pronunciation': VL_pro, 'Standard': VL_sta}
VL_word_df = pd.DataFrame(VL_word_data)

# 문장
VL_sentence_data = {'Dialect': VL_di, 'Pronunciation': VL_pr, 'Standard': VL_st}
VL_sentence_df = pd.DataFrame(VL_sentence_data)

# csv 파일 저장
VL_word_df.to_csv(r'C:\Users\edcrf\word_dataVL.csv', index=False)
VL_sentence_df.to_csv(r'C:\Users\edcrf\sentence_dataVL.csv', index=False)
 