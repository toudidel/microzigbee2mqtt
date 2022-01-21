package pl.micronaut.service.impl;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.micronaut.configuration.PushoverProperties;
import pl.micronaut.http.PushoverClient;
import pl.micronaut.model.PushoverMessage;
import pl.micronaut.service.PushoverService;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PushoverServiceImpl implements PushoverService {

  private final PushoverProperties pushoverProperties;
  private final PushoverClient pushoverClient;

  @Async
  @Override
  public void send(String title, String message) {
    if (!pushoverProperties.isEnabled()) {
      return;
    }

    pushoverClient.send(
        PushoverMessage.builder()
            .user(pushoverProperties.getUser())
            .token(pushoverProperties.getToken())
            .title(title)
            .message(message)
            .build());
  }
}
