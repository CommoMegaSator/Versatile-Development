package App.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    PREMIUM,
    USER,
    GUEST;

    @Override
    public String getAuthority() {
        return name();
    }
}
