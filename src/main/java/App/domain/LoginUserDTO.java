package App.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
public class LoginUserDTO {
    @NotNull(message = "Field 'nickname' can not be NULL")
    @Size(min = 4, max = 23)
    private String login;

    @NotNull(message = "Field 'password' can not be NULL")
    @Size(min = 8, max = 23)
    private String password;
}
