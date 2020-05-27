package versatile_development.domain.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
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
