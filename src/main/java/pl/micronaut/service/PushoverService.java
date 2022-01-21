package pl.micronaut.service;

/** Pushover service. */
public interface PushoverService {

  /**
   * Sends notification via Pushover.
   *
   * @param title
   * @param message message
   */
  void send(String title, String message);
}
