package versatile_development.domain;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ADMIN,
    PREMIUM,
    USER;

    @Override
    public String getAuthority() {
        return name();
    }
}
