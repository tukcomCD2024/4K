package springwebsocket.webchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springwebsocket.webchat.config.MemberConfig;

@Import(MemberConfig.class)
@SpringBootApplication
public class WebChatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebChatApplication.class, args);
    }

}
