package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.openclassrooms.mddapi.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceTests {

    @Mock
    private EntityManager entityManager;
    @Mock
    private Query query;
    @Mock
    private ArticleMapper articleMapper;
    @Mock
    private ArticleRepository articleRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ThemeRepository themeRepository;
    private ArticleService articleService;

    @BeforeEach
    void setup() {
        articleService = new ArticleService(articleMapper, entityManager, userRepository, themeRepository, articleRepository);
    }

    @Test
    void getAllArticlesOfUser() {
        User user = makeUser(1, false);
        Theme theme = makeTheme(1);
        List<Article> articleList = new ArrayList<>();
        List<ArticleDto> articleDtoList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Article article = makeArticle(i, theme, user);
            ArticleDto articleDto = makeArticleDto(i, theme, user);
            articleList.add(article);
            articleDtoList.add(articleDto);
        }
        when(entityManager.createNativeQuery(anyString(), eq(Article.class))).thenReturn(query);
        when(query.setParameter(eq("userId"), eq(user.getId()))).thenReturn(query);
        when(query.getResultList()).thenReturn(articleList);

        List<Article> articleCollectionByUser = articleService.findArticlesOfUser(user);

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        List<Article> articleCollectionByUserId = articleService.findArticlesOfUser(user.getId());

        assertFalse(articleDtoList.isEmpty());
        assertEquals(articleDtoList.size(), articleCollectionByUser.size());
        assertEquals(articleDtoList.size(), articleCollectionByUserId.size());

        assertThrows(
                ApiResourceNotFoundException.class,
                () -> articleService.findArticlesOfUser(654L)
        );

        assertThrows(
                ApiBadPostRequestException.class,
                () -> articleService.findArticlesOfUser("bad-request")
        );
    }

    @Test
    public void testFindArticle() {
        Theme theme = makeTheme(1);
        User author = makeUser(987, true);
        Article article = makeArticle(1, theme, author);

        when(articleRepository.findById(article.getId())).thenReturn(Optional.of(article));

        Article articleByString = articleService.findArticle(article.getId().toString());
        Article articleByLong = articleService.findArticle(article.getId().toString());

        assertEquals(articleByString.getId(), article.getId());
        assertEquals(articleByLong.getId(), article.getId());

        assertThrows(
                ApiResourceNotFoundException.class,
                () -> articleService.findArticle("7987")
        );

        assertThrows(
                ApiBadPostRequestException.class,
                () -> articleService.findArticle("bad-request")
        );
    }

    @Test
    public void testCreateArticle() {
        User user = makeUser(1, true);
        Theme theme = makeTheme(1);

        when(userRepository.existsById(user.getId())).thenReturn(true);
        when(themeRepository.existsById(theme.getId())).thenReturn(true);

        ArticleDto articleDto = new ArticleDto();
        articleDto.setAuthorId(user.getId());
        articleDto.setThemeId(theme.getId());

        Article article = new Article();
        article.setAuthor(user);
        article.setTheme(theme);

        when(articleMapper.toEntity(articleDto)).thenReturn(article);
        articleService.save(articleDto);
    }
}
