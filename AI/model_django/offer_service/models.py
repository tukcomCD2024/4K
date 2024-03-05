from django.db import models

# Create your models here.
from keras.models import load_model

class MyModel:
    _instance = None

    @staticmethod
    def get_instance():
        if MyModel._instance is None:
            MyModel._instance = load_model('offer_service/LSTM_model.h5')
        return MyModel._instance