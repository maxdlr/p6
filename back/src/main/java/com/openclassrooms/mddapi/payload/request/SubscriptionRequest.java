package com.openclassrooms.mddapi.payload.request;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class SubscriptionRequest {
  @NonNull Long userId;
  @NonNull Long themeId;
}
