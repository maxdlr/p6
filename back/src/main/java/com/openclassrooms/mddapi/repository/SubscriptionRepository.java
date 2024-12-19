package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import java.util.List;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  Boolean existsByUserAndTheme(User user, Theme theme);

  Optional<Subscription> findByUserAndTheme(User user, Theme theme);

  void removeById(Long id);

  boolean existsByUserIdAndThemeId(Long userId, Long themeId);

  List<Subscription> findAllByUser(@NonNull User user);
}
