package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Theme;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {}
