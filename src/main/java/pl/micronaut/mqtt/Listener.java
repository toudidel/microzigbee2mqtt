package pl.micronaut.mqtt;

import io.micronaut.mqtt.annotation.MqttSubscriber;
import io.micronaut.mqtt.annotation.Topic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.micronaut.model.Message;
import pl.micronaut.service.CoordinatorService;

@Slf4j
@RequiredArgsConstructor
@MqttSubscriber
public class Listener {

  private final CoordinatorService coordinatorService;

  @Topic("${zigbee2mqtt.queue-prefix}#")
  public void receive(@Topic String topic, Message message) {
    coordinatorService.process(topic, message);
  }
}
