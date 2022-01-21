package pl.micronaut.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.Introspected;
import javax.validation.Valid;
import lombok.Data;

@Data
@Context
@ConfigurationProperties("email")
public class EmailProperties {

  private boolean enabled;
  private String recipient;
  @Valid private Smtp smtp;

  @Data
  @Introspected
  @ConfigurationProperties("smtp")
  public static class Smtp {
    private String host;
    private int port;
    private boolean ssl;
    private String username;
    private String password;
  }
}
