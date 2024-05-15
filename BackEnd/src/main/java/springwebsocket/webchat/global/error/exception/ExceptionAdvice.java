package springwebsocket.webchat.global.error.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.global.error.BusinessException;
import springwebsocket.webchat.global.response.ErrorCode;
import springwebsocket.webchat.global.response.ErrorResponse;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class ExceptionAdvice {

	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleRuntimeException(BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse response =
			ErrorResponse.builder()
				.errorMessage(errorCode.getMessage())
				.build();
		log.warn(e.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(response);
	}
}
