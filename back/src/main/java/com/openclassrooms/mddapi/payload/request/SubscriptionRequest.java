package com.openclassrooms.mddapi.payload.request;

import lombok.*;
import lombok.experimental.Accessors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SubscriptionRequest {
  @NonNull Long userId;
  @NonNull Long themeId;
}
