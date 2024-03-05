from django.shortcuts import render
from django.http import JsonResponse

from rest_framework.decorators import api_view
from keras.models import load_model

from . preprocess import preprocess_input
import traceback    # 에러 로깅

# 모델 로드
model = load_model('offer_service/LSTM_model.h5')

# 방언 문장을 변경하기 위해 POST 응답만 받음
@api_view(['POST'])
def convert_dialect_to_standard(request):
    if request.method == 'POST':
        dialect_text = request.data.get('dialect_text', '')  

        try:
            preprocess_text = preprocess_input(dialect_text)
        except Exception as e:
            traceback.print_exc()
            return JsonResponse({'error': '전처리 중 오류 발생'}, status=400)

        try:
            standard_text = model.predict([preprocess_text])[0]
        except Exception as e:
            traceback.print_exc()
            return JsonResponse({'error': '모델 예측 중 오류 발생'}, status=500)
        
        return JsonResponse({'standard_text': standard_text})
    else:
        return JsonResponse({'error': '요청 실패'}, status=405) 