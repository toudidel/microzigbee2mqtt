package pl.micronaut.model;

import lombok.Builder;
import lombok.Data;
import pl.micronaut.configuration.ApplicationProperties;

@Data
@Builder
public class PublisherMessage {

  private String triggeringDevice;

  private String triggeredDevice;

  private String topic; // MQTT topic

  private Message message; // MQTT message

  private ApplicationProperties.Notification notification; // notification settings
}
