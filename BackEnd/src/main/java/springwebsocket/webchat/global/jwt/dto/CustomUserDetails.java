package springwebsocket.webchat.global.jwt.dto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import springwebsocket.webchat.member.entity.Member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Slf4j
public class CustomUserDetails implements UserDetails {


    private final Optional<Member> member;

    public CustomUserDetails(Optional<Member> member) {
        this.member = member;
    }

    //role값 반환하는 함수
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.get().getRole();
            }
        });

        return collection;
    }

    public String getEmail() {
        return member.get().getEmail();
    }


    @Override
    public String getPassword() {
        return member.get().getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }



    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
