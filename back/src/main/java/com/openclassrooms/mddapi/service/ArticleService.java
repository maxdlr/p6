package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.dto.ArticleDto;
import com.openclassrooms.mddapi.exception.ApiBadPostRequestException;
import com.openclassrooms.mddapi.exception.ApiResourceNotFoundException;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.ThemeRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {
    private final ArticleMapper articleMapper;
    private final EntityManager entityManager;
    private final UserRepository userRepository;
    private final ThemeRepository themeRepository;
    private final ArticleRepository articleRepository;

    public ArticleService(ArticleMapper articleMapper, EntityManager entityManager, UserRepository userRepository, ThemeRepository themeRepository, ArticleRepository articleRepository) {
        this.articleMapper = articleMapper;
        this.entityManager = entityManager;
        this.userRepository = userRepository;
        this.themeRepository = themeRepository;
        this.articleRepository = articleRepository;
    }

    private List<Article> executeFindArticlesByUserQuery(User user) {
        String nativeSql =
                """
                            SELECT a.*
                            FROM ARTICLES a
                            JOIN THEMES t ON a.theme_id = t.id
                            JOIN SUBSCRIPTIONS s ON s.theme_id = t.id
                            WHERE s.user_id = :userId
                        """;

        Query query = entityManager.createNativeQuery(nativeSql, Article.class);
        query.setParameter("userId", user.getId());

        return query.getResultList();
    }

    public List<Article> findArticlesOfUser(User user) {
        return executeFindArticlesByUserQuery(user);
    }

    public List<Article> findArticlesOfUser(Long id) {
        Optional<User> user = userRepository.findById(id);

        if (user.isEmpty()) {
            throw new ApiResourceNotFoundException("Cannot find user");
        }

        return executeFindArticlesByUserQuery(user.get());
    }

    public List<Article> findArticlesOfUser(String id) {
        try {
            Optional<User> user = userRepository.findById(Long.valueOf(id));

            if (user.isEmpty()) {
                throw new ApiResourceNotFoundException("Cannot find user");
            }

            return executeFindArticlesByUserQuery(user.get());
        } catch (NumberFormatException e) {
            throw new ApiBadPostRequestException(e.getMessage());
        }
    }

    public void save(ArticleDto articleDto) {
        try {
            if (!userRepository.existsById(articleDto.getAuthorId())) {
                throw new ApiResourceNotFoundException("Cannot find author of article");
            }

            if (!themeRepository.existsById(articleDto.getThemeId())) {
                throw new ApiResourceNotFoundException("Cannot find associated theme of article");
            }

            Article article = articleMapper.toEntity(articleDto);
            articleRepository.save(article);
        } catch (HttpMessageNotReadableException e) {
            throw new ApiBadPostRequestException(e.getMessage());
        }
    }

    public Article findArticle(String id) {
        try {
            Optional<Article> article = articleRepository.findById(Long.valueOf(id));

            if (article.isEmpty()) {
                throw new ApiResourceNotFoundException("Cannot find article");
            }

            return article.get();
        } catch (NumberFormatException e) {
            throw new ApiBadPostRequestException(e.getMessage());
        }
    }

    public Article findArticle(Long id) {
        Optional<Article> article = articleRepository.findById(id);

        if (article.isEmpty()) {
            throw new ApiResourceNotFoundException("Cannot find article");
        }

        return article.get();
    }
}
