package versatile_development.utils;

import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.entity.ArticleEntity;


import java.util.List;

@Mapper(componentModel = "spring")
public interface ArticleMapper {
    ArticleEntity toEntity(ArticleDTO articleDTO);

    ArticleDTO toDto(ArticleEntity articleEntity);

    List<ArticleDTO> toDtoList(List<ArticleEntity> articleEntityList);

    List<ArticleEntity> toEntityList(List<ArticleDTO> articleDTOList);
}
