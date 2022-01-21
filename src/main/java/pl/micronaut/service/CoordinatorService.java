package pl.micronaut.service;

import pl.micronaut.model.Message;

/** Coordinator service. */
public interface CoordinatorService {

  /**
   * Processes single message from topic.
   *
   * @param topic topic
   * @param message message
   */
  void process(String topic, Message message);
}
