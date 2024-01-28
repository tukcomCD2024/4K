package springwebsocket.webchat.service.login;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import springwebsocket.webchat.dao.User;
import springwebsocket.webchat.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class loginService {

    private final UserRepository repository;

    public String login(String loginEmail, String password) {
        User user = repository.findByLoginEmail(loginEmail)
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
