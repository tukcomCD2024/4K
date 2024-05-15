package springwebsocket.webchat.friend.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import springwebsocket.webchat.friend.exception.FriendDuplicationException;
import springwebsocket.webchat.global.response.ResultResponse;


@RestControllerAdvice
@Slf4j
public class FriendExceptionController {

    @ExceptionHandler(FriendDuplicationException.class)
    public ResponseEntity<ResultResponse> DataIntegrityViolationException(FriendDuplicationException e){
        log.info("DataIntegrityViolationException");
        ResultResponse exceptionResponse = new ResultResponse(e.getErrorCode());
        return ResponseEntity.status(exceptionResponse.getStatus()).body(exceptionResponse);
    }
}
