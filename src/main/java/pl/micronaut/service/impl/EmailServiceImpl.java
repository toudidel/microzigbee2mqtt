package pl.micronaut.service.impl;

import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import pl.micronaut.configuration.EmailProperties;
import pl.micronaut.service.EmailService;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final EmailProperties emailProperties;

  @Async
  @Override
  public void send(String subject, String text) {
    if (!emailProperties.isEnabled()) {
      return;
    }

    try {
      HtmlEmail email = new HtmlEmail();
      email.setHostName(emailProperties.getSmtp().getHost());
      email.setSmtpPort(emailProperties.getSmtp().getPort());
      email.setSSLOnConnect(emailProperties.getSmtp().isSsl());
      email.setAuthentication(
          emailProperties.getSmtp().getUsername(), emailProperties.getSmtp().getPassword());
      email.setFrom(emailProperties.getSmtp().getUsername());
      email.addTo(emailProperties.getRecipient());
      email.setSubject(subject);
      email.setTextMsg(text);
      email.send();
    } catch (EmailException e) {
      log.warn("An error occured during sending email: " + e.getMessage(), e);
    }
  }
}
