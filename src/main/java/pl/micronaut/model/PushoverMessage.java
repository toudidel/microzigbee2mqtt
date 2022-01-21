package pl.micronaut.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PushoverMessage {

  private String user;
  private String token;
  private String title;
  private String message;
  private int priority = 0;
}
