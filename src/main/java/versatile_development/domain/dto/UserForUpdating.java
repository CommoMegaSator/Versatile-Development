package versatile_development.domain.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties
public class UserForUpdating implements Serializable {

    @Size(min = 4, max = 25)
    private String firstname;

    @Size(min = 4, max = 25)
    private String lastname;

    @Email
    @Size(min = 4, max = 25)
    private String email;

    @Size(min = 8, max = 25)
    private String password;

    private String birthday;

    private String gender;

    private String nationality;

    private String aboutUser;
}
