package versatile_development.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Проміжний об'єкт користувача")
public class IntermediateUser {
    @Schema(description = "Псевдонім")
    String nickname;
    @Schema(description = "Токен")
    String token;
}
