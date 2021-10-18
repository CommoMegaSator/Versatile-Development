package versatile_development.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Schema(description = "Модель статті")
public class ArticleDTO {
    @Schema(description = "Ідентифікатор")
    private Long id;

    @NotNull
    @Schema(description = "Назва")
    private String title;

    @NotNull
    @Schema(description = "Дата створення")
    private Date creation_date;

    @NotNull
    @Schema(description = "Текст статті")
    private String text;

    @NotNull
    @Schema(description = "Автор")
    private UserDTO user;
}
