package versatile_development.service.impl;

import versatile_development.domain.ArticleDTO;
import versatile_development.entity.ArticleEntity;
import versatile_development.repository.ArticleRepository;
import versatile_development.service.ArticleService;
import versatile_development.utils.ObjectMapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    private ObjectMapperUtils modelMapper;

    @Override
    public void createArticle(ArticleDTO articleDTO) {
        ArticleEntity article = DTOToEntityMapper(articleDTO);
        articleRepository.save(article);
    }

    @Override
    public List<ArticleDTO> findAllArticles() {
        List<ArticleEntity> listOfArticles = (List<ArticleEntity>) articleRepository.findAll();
        List<ArticleDTO> articleDTOs = new ArrayList<>();

        for (ArticleEntity articleEntity : listOfArticles){
            articleDTOs.add(entityToDTOMapper(articleEntity));
        }

        return articleDTOs;
    }

    public ArticleDTO entityToDTOMapper(ArticleEntity articleEntity){
        ArticleDTO articleDTO = modelMapper.map(articleEntity, ArticleDTO.class);
        articleDTO.setId(articleEntity.getId());
        return articleDTO;
    }

    public ArticleEntity DTOToEntityMapper(ArticleDTO articleDTO){
        return modelMapper.map(articleDTO, ArticleEntity.class);
    }
}
