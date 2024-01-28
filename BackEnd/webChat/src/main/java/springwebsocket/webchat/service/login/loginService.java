package springwebsocket.webchat.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.entity.Member;
import springwebsocket.webchat.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class loginService {

    private final MemberRepository repository;

    public String login(String loginEmail, String password) {
        Member user = repository.findByLoginEmail(loginEmail)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);

        if (user == null) {
            return "fail";
        }
        else{
            return "success";
        }
    }

}
