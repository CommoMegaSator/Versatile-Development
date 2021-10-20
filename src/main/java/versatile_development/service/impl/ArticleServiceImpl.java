package versatile_development.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.entity.ArticleEntity;
import versatile_development.repository.ArticleRepository;
import versatile_development.service.ArticleService;
import versatile_development.utils.ObjectMapperUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final ObjectMapperUtils modelMapper;

    @Autowired
    ArticleServiceImpl(@Qualifier(value = "articleRepository") ArticleRepository articleRepository,
                       @Qualifier(value = "objectMapperUtils") ObjectMapperUtils modelMapper){
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public void createArticle(ArticleDTO articleDTO) {
        var articleEntity = DTOToEntityMapper(articleDTO);
        articleRepository.save(articleEntity);
    }

    @Override
    public List<ArticleDTO> findAllArticles() {
        var listOfArticles = articleRepository.findAll();
        var articleDTOs = new ArrayList<ArticleDTO>();

        for (ArticleEntity articleEntity : listOfArticles){
            articleDTOs.add(entityToDTOMapper(articleEntity));
        }

        return articleDTOs;
    }

    public ArticleDTO entityToDTOMapper(ArticleEntity articleEntity){
        var articleDTO = modelMapper.map(articleEntity, ArticleDTO.class);
        articleDTO.setId(articleEntity.getId());
        return articleDTO;
    }

    public ArticleEntity DTOToEntityMapper(ArticleDTO articleDTO){
        return modelMapper.map(articleDTO, ArticleEntity.class);
    }
}
