package pl.micronaut.configuration;

import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import lombok.Data;

@Data
@Context
@ConfigurationProperties("pushover")
public class PushoverProperties {

  private boolean enabled = false;
  private String user;
  private String token;
}
