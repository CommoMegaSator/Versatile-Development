package versatile_development.service;

import versatile_development.domain.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {
    void createArticle(ArticleDTO article);
    List<ArticleDTO> findAllArticles();
}
