package versatile_development.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.entity.ArticleEntity;
import versatile_development.repository.ArticleRepository;
import versatile_development.service.ArticleService;
import versatile_development.mapper.ArticleMapper;

import java.util.ArrayList;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final ArticleMapper articleMapper;

    @Autowired
    ArticleServiceImpl(@Qualifier(value = "articleRepository") ArticleRepository articleRepository,
                       ArticleMapper articleMapper){
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
    }

    @Override
    public void createArticle(ArticleDTO articleDTO) {
        var articleEntity = articleMapper.dtoToEntity(articleDTO);
        articleRepository.save(articleEntity);
    }

    @Override
    public List<ArticleDTO> findAllArticles() {
        var listOfArticles = articleRepository.findAll();
        var articleDTOs = new ArrayList<ArticleDTO>();

        for (ArticleEntity articleEntity : listOfArticles){
            articleDTOs.add(articleMapper.entityToDto(articleEntity));
        }

        return articleDTOs;
    }
}
