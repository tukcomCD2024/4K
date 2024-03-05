from django.shortcuts import render
from django.http import JsonResponse

from rest_framework.decorators import api_view
from keras.models import load_model

from . preprocess import preprocess_input
from background_task import background, Task
import traceback    # 에러 로깅

model = None

# 모델을 로드하는 함수를 백그라운드로 실행해 모델을 비동기적으로 로드
# 서버가 실행될때 백그라운드에서 모델 로딩이 처리되어 응답 시간 단축
@background(schedule=60)
def load_model_function():
    global model
    if model is None:
        model = load_model('offer_service/LSTM_model.h5')

def check_model_load():
    tasks = Task.objects.filter(task_name="offer_service.views.load_model_function")
    if tasks.exists():
        return tasks[0].is_done
    return False

load_model_function()

# 방언 문장을 변경하기 위해 POST 응답만 받음
@api_view(['POST'])
def convert_dialect_to_standard(request):
    global model
    if request.method == 'POST':
        dialect_text = request.data.get('dialect_text', '')  

        try:
            preprocess_text = preprocess_input(dialect_text)
        except Exception as e:
            traceback.print_exc()
            return JsonResponse({'error': '전처리 중 오류 발생'}, status=400)

        try:
            if not check_model_load():
                return JsonResponse({'error': '모델 로드 중, 잠시 후 다시 시도하세요.'}, status=503)
            else:
                standard_text = model.predict([preprocess_text])[0]
        except Exception as e:
            traceback.print_exc()
            return JsonResponse({'error': '모델 예측 중 오류 발생'}, status=500)
        
        return JsonResponse({'standard_text': standard_text})
    else:
        return JsonResponse({'error': '요청 실패'}, status=405) 