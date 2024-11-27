package com.openclassrooms.mddapi.models;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.Accessors;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(
    name = "USERS",
    uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
@EqualsAndHashCode(of = {"id"})
@Builder
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
@ToString
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NonNull
  @Size(max = 50)
  @Email
  private String email;

  @NonNull
  @Size(max = 20)
  @Column(name = "username")
  private String username;

  @NonNull
  @Size(max = 120)
  private String password;

  @CreatedDate
  @Column(name = "created_at", updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  public Long getId() {
    return id;
  }

  public User setId(Long id) {
    this.id = id;
    return this;
  }

  public @NonNull String getEmail() {
    return email;
  }

  public User setEmail(@NonNull String email) {
    this.email = email;
    return this;
  }

  public @NonNull String getUsername() {
    return username;
  }

  public User setUsername(@NonNull String username) {
    this.username = username;
    return this;
  }

  public @NonNull String getPassword() {
    return password;
  }

  public User setPassword(@NonNull String password) {
    this.password = password;
    return this;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public User setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
    return this;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public User setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
    return this;
  }
}
