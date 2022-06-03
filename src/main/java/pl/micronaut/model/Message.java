package pl.micronaut.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import org.apache.commons.lang3.BooleanUtils;
import pl.micronaut.util.StringCommonUtils;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message {

  private String scene;
  private Integer voltage;
  private Integer battery;
  private Integer linkquality;
  private String click;
  private Boolean scheduled;
  private String action;
  private String occupancy;
  private String state;
  private String state_l1;
  private String state_l2;
  private String state_l3;
  private String state_l4;
  private Double current;
  private Double energy;
  private Double humidity;
  private Double temperature;
  private Integer pressure;
  private Integer illuminance;
  private Integer illuminanceLux;
  private Integer brightness;

  @JsonProperty("color_mode")
  private String colorMode;

  @JsonProperty("color_temp")
  private Integer colorTemp;

  private Boolean tamper;

  @JsonProperty("water_leak")
  private Boolean waterLeak;

  @JsonProperty("battery_low")
  private Boolean batteryLow;

  private Boolean alarm;
  private Boolean contact;

  public boolean matches(String property, String value) {
    switch (property) {
      case MessageProperty.ACTION:
        return StringCommonUtils.valueOf(action).equals(value);
      case MessageProperty.OCCUPANCY:
        return StringCommonUtils.valueOf(occupancy).equals(value);
      case MessageProperty.STATE:
        return StringCommonUtils.valueOf(state).equals(value);
      case MessageProperty.STATE_L1:
        return StringCommonUtils.valueOf(state_l1).equals(value);
      case MessageProperty.STATE_L2:
        return StringCommonUtils.valueOf(state_l2).equals(value);
      case MessageProperty.STATE_L3:
        return StringCommonUtils.valueOf(state_l3).equals(value);
      case MessageProperty.STATE_L4:
        return StringCommonUtils.valueOf(state_l4).equals(value);
      case MessageProperty.WATER_LEAK:
        return BooleanUtils.compare(BooleanUtils.isTrue(waterLeak), BooleanUtils.toBoolean(value))
            == 0;
    }
    return false;
  }

  public static class MessageBuilder {

    public MessageBuilder propertyValue(String property, String value) {
      switch (property) {
        case MessageProperty.ACTION:
          this.action = value;
          break;
        case MessageProperty.STATE:
          this.state = value;
          break;
        case MessageProperty.STATE_L1:
          this.state_l1 = value;
          break;
        case MessageProperty.STATE_L2:
          this.state_l2 = value;
          break;
        case MessageProperty.STATE_L3:
          this.state_l3 = value;
          break;
        case MessageProperty.STATE_L4:
          this.state_l4 = value;
          break;
        case MessageProperty.ALARM:
          this.alarm = BooleanUtils.toBooleanObject(value);
          break;
      }
      return this;
    }
  }
}
