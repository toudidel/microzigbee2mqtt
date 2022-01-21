package pl.micronaut.configuration;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.context.annotation.ConfigurationProperties;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.Introspected;
import lombok.Data;
import net.redhogs.cronparser.CronExpressionDescriptor;
import net.redhogs.cronparser.Options;
import pl.micronaut.validation.DevicesConstraint;
import pl.micronaut.validation.ScenesConstraint;
import pl.micronaut.validation.TriggersConstraint;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@Data
@Context
@ConfigurationProperties("zigbee2mqtt")
public class ApplicationProperties {

  private String queuePrefix;
  @Valid @DevicesConstraint private List<Device> devices;
  @Valid @ScenesConstraint private List<Scene> scenes;

  @Data
  @Introspected
  public static class Device {
    @NotBlank private String name;
  }

  @Data
  @Introspected
  public static class Scene {
    @NotBlank private String name;
    private Boolean enabled;
    private String device;
    private String cron;
    private String property;
    private String value;
    @Valid Notification notification;
    @Valid @TriggersConstraint private List<Trigger> triggers;

    @JsonIgnore
    public String getDeviceOrCron() {
      try {
        return device != null
            ? device
            : CronExpressionDescriptor.getDescription(cron, Options.twentyFourHour());
      } catch (ParseException e) {
        return Optional.ofNullable(device).orElse(cron);
      }
    }
  }

  @Data
  @Introspected
  public static class Notification {
    private Boolean email;
    private Boolean pushover;
  }

  @Data
  @Introspected
  public static class Trigger {
    private String doNotDisable; // do not disable on nested messages
    private String disable; // name of scene to disable
    private String enable; // name of scene to enable
    private String device;
    private String property;
    private String value;
  }
}
