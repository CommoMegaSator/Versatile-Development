package versatile_development.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import versatile_development.domain.dto.ArticleDTO;
import versatile_development.entity.ArticleEntity;
import versatile_development.entity.UserEntity;
import versatile_development.mapper.ArticleMapper;
import versatile_development.repository.ArticleRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ContextConfiguration(classes = {ArticleRepository.class, ArticleServiceImpl.class, ArticleMapper.class})
@ExtendWith(SpringExtension.class)
public class ArticleServiceImplTest {
    @MockBean
    private ArticleRepository articleRepository;

    @Autowired
    private ArticleServiceImpl articleServiceImpl;

    @MockBean
    private ArticleMapper articleMapper;

    @Test
    public void testCreateArticle() {
        UserEntity userEntity = new UserEntity();
        LocalDateTime atStartOfDayResult = LocalDate.of(1970, 1, 1).atStartOfDay();
        userEntity.setCreationDate(Date.from(atStartOfDayResult.atZone(ZoneId.systemDefault()).toInstant()));
        userEntity.setFriendsNumber(3);
        userEntity.setEmail("jane.doe@example.org");
        userEntity.setPassword("iloveyou");
        LocalDateTime atStartOfDayResult1 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userEntity.setBirthday(Date.from(atStartOfDayResult1.atZone(ZoneId.systemDefault()).toInstant()));
        userEntity.setNickname("Nickname");
        userEntity.setAge(1);
        LocalDateTime atStartOfDayResult2 = LocalDate.of(1970, 1, 1).atStartOfDay();
        userEntity.setTokenExpiration(Date.from(atStartOfDayResult2.atZone(ZoneId.systemDefault()).toInstant()));
        userEntity.setFirstname("Jane");
        userEntity.setConfirmationToken("ABC123");
        userEntity.setActivated(true);
        userEntity.setGender("Gender");
        userEntity.setCity("Oxford");
        userEntity.setId(123L);
        userEntity.setNationality("Nationality");
        userEntity.setAboutUser("About User");
        userEntity.setReports(1);
        userEntity.setArticle(new ArrayList<>());
        userEntity.setLastname("Doe");
        userEntity.setRoles(new HashSet<>());

        var articleEntity = new ArticleEntity();
        articleEntity.setText("Text");
        LocalDateTime atStartOfDayResult3 = LocalDate.of(1970, 1, 1).atStartOfDay();
        articleEntity.setCreation_date(Date.from(atStartOfDayResult3.atZone(ZoneId.systemDefault()).toInstant()));
        articleEntity.setId(123L);
        articleEntity.setUser(userEntity);
        articleEntity.setTitle("Dr");
        when(this.articleRepository.save(any())).thenReturn(articleEntity);
        this.articleServiceImpl.createArticle(new ArticleDTO());
        verify(this.articleRepository).save(any());
    }

    @Test
    public void testFindAllArticles() {
        when(this.articleRepository.findAll()).thenReturn(new ArrayList<>());
        assertTrue(this.articleServiceImpl.findAllArticles().isEmpty());
        verify(this.articleRepository).findAll();
    }

    @Test
    public void testDTOToEntityMapper() {
        assertNull(this.articleServiceImpl.DTOToEntityMapper(new ArticleDTO()));
    }
}

