package App.service;

import App.domain.ArticleDTO;

import java.util.List;

public interface ArticleService {
    void createArticle(ArticleDTO article);
    List<ArticleDTO> findAllArticles();
}
