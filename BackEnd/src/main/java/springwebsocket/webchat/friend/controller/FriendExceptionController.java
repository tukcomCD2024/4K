package springwebsocket.webchat.friend.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.friend.exception.FriendDuplicationException;
import springwebsocket.webchat.global.exception.ExceptionResponse;


@RestControllerAdvice
@Slf4j
public class FriendExceptionController {

    @ExceptionHandler(FriendDuplicationException.class)
    public ResponseEntity<ExceptionResponse> DataIntegrityViolationException(FriendDuplicationException e){
        ExceptionResponse exceptionResponse = new ExceptionResponse(e.getErrorCode());
        return ResponseEntity.ok(exceptionResponse);
    }
}
