from django.urls import path
from .views import convert_dialect_to_standard

urlpatterns = [
    path('convert', convert_dialect_to_standard),
]