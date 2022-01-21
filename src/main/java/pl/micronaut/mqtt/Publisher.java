package pl.micronaut.mqtt;

import io.micronaut.mqtt.annotation.Topic;
import io.micronaut.mqtt.v5.annotation.MqttPublisher;
import io.micronaut.scheduling.annotation.Async;
import pl.micronaut.model.Message;

@MqttPublisher
public interface Publisher {

  @Async
  void send(@Topic String topic, Message message);
}