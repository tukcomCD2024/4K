package springwebsocket.webchat.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ApiResponse;
import springwebsocket.webchat.global.response.ErrorCode;


@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler(RuntimeException.class)
	public ResponseEntity<ApiResponse<?>> handleDuplicatedUserException(BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();

		return ResponseEntity.status(HttpStatus.CONFLICT).body(ApiResponse.createError(errorCode.getMessage()));
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationExceptions(BindingResult bindingResult) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.createFail(bindingResult));
	}
}