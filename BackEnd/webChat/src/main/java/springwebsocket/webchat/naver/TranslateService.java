package springwebsocket.webchat.naver;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class TranslateService {

    @Value("${spring.naver.client-id}")
    private String clientId;
    @Value("${spring.naver.client-secret}")
    private String clientSecret;


    public TranslateResponseDto.Result naverPapagoTranslate(String source, String target, String text) {

        WebClient webClient = WebClient.builder()
                .baseUrl("https://naveropenapi.apigw.ntruss.com/nmt/v1/translation")
                .build();
        TranslateResponseDto response = webClient.post().uri(
                        uriBuilder -> uriBuilder.queryParam("source", source)
                                .queryParam("target", target)
                                .queryParam("text", text)
                                .build()
                )
                .header("X-NCP-APIGW-API-KEY-ID", clientId)
                .header("X-NCP-APIGW-API-KEY", clientSecret)
                .header("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                .retrieve()
                .bodyToMono(TranslateResponseDto.class).block();
        return response.getMessage().getResult();
    }
}
