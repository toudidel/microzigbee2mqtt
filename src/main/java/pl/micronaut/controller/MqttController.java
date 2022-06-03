package pl.micronaut.controller;

import io.micronaut.context.annotation.Value;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.micronaut.model.Message;
import pl.micronaut.model.PublisherMessage;
import pl.micronaut.service.PublisherService;

@Slf4j
@Controller("/mqtt")
@RequiredArgsConstructor
public class MqttController {

  private final PublisherService publisherService;

  @Value("${zigbee2mqtt.queue-prefix}")
  public String queuePrefix;

  @Get()
  public void propertyValue(
      @QueryValue String queue, @QueryValue String property, @QueryValue String value) {
    publisherService.send(
        PublisherMessage.builder()
            .topic(queuePrefix + queue)
            .message(Message.builder().propertyValue(property, value).build())
            .build());
  }
}
