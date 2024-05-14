package springwebsocket.webchat.member.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.member.exception.EmailDuplicatedException;
import springwebsocket.webchat.global.exception.ExceptionResponse;

@RestControllerAdvice
@Slf4j
public class ExceptionController {
    // 400

    @ExceptionHandler(EmailDuplicatedException.class)
    public ResponseEntity<ExceptionResponse> EmailDuplicatedException(EmailDuplicatedException e) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getErrorCode());
        return ResponseEntity.ok(exceptionResponse);
    }
}
