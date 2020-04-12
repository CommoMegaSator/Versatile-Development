package versatile_development.domain.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class ArticleDTO {
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private Date creation_date;

    @NotNull
    private String text;

    @NotNull
    private UserDTO user;
}
