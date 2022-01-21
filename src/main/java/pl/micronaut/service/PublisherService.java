package pl.micronaut.service;

import pl.micronaut.model.PublisherMessage;

/** Publisher service. */
public interface PublisherService {

  /**
   * Publishes message to mqtt broker.
   *
   * @param publisherMessage message
   */
  void send(PublisherMessage publisherMessage);
}
