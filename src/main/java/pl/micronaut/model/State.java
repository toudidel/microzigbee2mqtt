package pl.micronaut.model;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class State {

  private String lastModifyDate;

  private String lastModifyDevice;

  private Map<String, DeviceMessage> deviceMessages;

  @Data
  @Builder
  public static class DeviceMessage {

    private String modifyDate;

    private Message message;
  }

  @Data
  @Builder
  public static class Voltage {

    private String device;

    private Integer voltage;
  }
}
