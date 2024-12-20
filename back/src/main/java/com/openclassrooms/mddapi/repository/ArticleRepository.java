package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Article;
import com.openclassrooms.mddapi.models.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
  List<Article> findAllByTheme(Theme theme);

  List<Article> findByTheme(Theme theme);

  Theme theme(Theme theme);
}
