package versatile_development.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.domain.dto.UserDTO;
import versatile_development.service.ArticleService;

import java.util.Date;

@Slf4j
@Controller
@RequestMapping("/articles")
public class ArticlesController {

    ArticleService articleService;

    @Autowired
    ArticlesController(ArticleService articleService){
        this.articleService = articleService;
    }

    @GetMapping
    public String getAllArticles(){
        return "articles";
    }

    @PostMapping
    public String createArticle(@AuthenticationPrincipal UserDTO user,
                                @RequestParam("title") String articleTitle,
                                @RequestParam("text") String articleText){
        ArticleDTO article = new ArticleDTO();
        article.setCreation_date(new Date());
        article.setTitle(articleTitle);
        article.setText(articleText);
        article.setUser(user);
        articleService.createArticle(article);

        log.info("Article named " + articleTitle + " was created");
        return "redirect:/articles";
    }
}
