package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.models.Subscription;
import com.openclassrooms.mddapi.models.Theme;
import com.openclassrooms.mddapi.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
  Boolean existsByUserAndTheme(User user, Theme theme);

  Optional<Subscription> findByUserAndTheme(User user, Theme theme);

  void removeBy(Subscription subscription);
}
