package springwebsocket.webchat.naver;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TranslateResponseDto {

    private Message message;

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class Message {
        private Result result;
    }

    @AllArgsConstructor
    @Getter
    @NoArgsConstructor
    public static class Result {
        private String srcLangType;
        private String tarLangType;
        private String translatedText;
    }

}