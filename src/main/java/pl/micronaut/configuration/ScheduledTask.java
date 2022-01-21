package pl.micronaut.configuration;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.micronaut.configuration.ApplicationProperties.Scene;
import pl.micronaut.model.Message;
import pl.micronaut.model.PublisherMessage;
import pl.micronaut.service.PublisherService;

@Slf4j
@Data
@Builder
@RequiredArgsConstructor
public class ScheduledTask implements Runnable {

  private final ApplicationProperties properties;
  private final PublisherService publisherService;
  private final Scene scene;

  @Override
  public void run() {
    scene.getTriggers().stream()
        .forEach(
            trigger ->
                publisherService.send(
                    PublisherMessage.builder()
                        .triggeringDevice(scene.getDeviceOrCron())
                        .triggeredDevice(trigger.getDevice())
                        .topic(properties.getQueuePrefix() + trigger.getDevice())
                        .notification(scene.getNotification())
                        .message(
                            Message.builder()
                                .scene(scene.getName())
                                .propertyValue(trigger.getProperty(), trigger.getValue())
                                .build())
                        .build()));
  }
}
