package versatile_development.mapper;

import org.mapstruct.Mapper;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.entity.ArticleEntity;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleEntity dtoToEntity(ArticleDTO articleDTO);

    ArticleDTO entityToDto(ArticleEntity articleEntity);

    List<ArticleDTO> entityListToDtoList(List<ArticleEntity> articleEntityList);

    List<ArticleEntity> dtoListToEntityList(List<ArticleDTO> articleDTOList);
}
