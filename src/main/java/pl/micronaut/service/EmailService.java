package pl.micronaut.service;

/** Email service. */
public interface EmailService {

  /**
   * Sends email.
   *
   * @param subject subject
   * @param text message text
   */
  void send(String subject, String text);
}
