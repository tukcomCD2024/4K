package springwebsocket.webchat.naver;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class TranslateController {
    private final TranslateService translateService;


    @PostMapping("/api/translate")
    public TranslateResponseDto.Result naverPapagoTranslate(String source, String target, String text) {
        return translateService.naverPapagoTranslate(source, target, text);
    }
}