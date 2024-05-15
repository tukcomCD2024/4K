package springwebsocket.webchat.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.global.response.ResultResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    // 400

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<ResultResponse> EmailDuplicatedException(EmailDuplicatedException e) {
        ResultResponse exceptionResponse = new ResultResponse(e.getErrorCode());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }
}
