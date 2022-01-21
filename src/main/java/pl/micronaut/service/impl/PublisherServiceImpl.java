package pl.micronaut.service.impl;

import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import pl.micronaut.configuration.ApplicationProperties;
import pl.micronaut.model.PublisherMessage;
import pl.micronaut.mqtt.Publisher;
import pl.micronaut.service.EmailService;
import pl.micronaut.service.PublisherService;
import pl.micronaut.service.PushoverService;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class PublisherServiceImpl implements PublisherService {

  private final Publisher publisher;
  private final EmailService emailService;
  private final PushoverService pushoverService;

  @Override
  public void send(PublisherMessage publisherMessage) {
    if (log.isDebugEnabled()) {
      log.debug(
          "PUB | "
              + publisherMessage.getTopic()
              + " | "
              + publisherMessage.getMessage().toString());
    }

    publisher.send(publisherMessage.getTopic(), publisherMessage.getMessage());

    // Send notifications
    ApplicationProperties.Notification notification = publisherMessage.getNotification();
    if (notification != null) {
      if (BooleanUtils.isTrue(notification.getEmail())) {
        emailService.send(publisherMessage.getTopic(), publisherMessage.getMessage().toString());
      }

      if (BooleanUtils.isTrue(notification.getPushover())) {
        pushoverService.send(
            publisherMessage.getMessage().getScene(),
            String.format(
                "%s -> %s",
                publisherMessage.getTriggeringDevice(), publisherMessage.getTriggeredDevice()));
      }
    }
  }
}
