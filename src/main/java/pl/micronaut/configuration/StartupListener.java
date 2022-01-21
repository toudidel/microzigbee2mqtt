package pl.micronaut.configuration;

import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.server.event.ServerStartupEvent;
import io.micronaut.scheduling.TaskScheduler;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import pl.micronaut.service.PublisherService;

import java.util.Objects;

@Singleton
@RequiredArgsConstructor
public class StartupListener implements ApplicationEventListener<ServerStartupEvent> {

  private final ApplicationProperties properties;
  private final TaskScheduler taskScheduler;
  private final PublisherService publisherService;

  @Override
  public void onApplicationEvent(ServerStartupEvent event) {
    properties.getScenes().stream()
        .filter(scene -> Objects.nonNull(scene.getCron()))
        .forEach(
            scene ->
                taskScheduler.schedule(
                    scene.getCron(),
                    ScheduledTask.builder()
                        .properties(properties)
                        .publisherService(publisherService)
                        .scene(scene)
                        .build()));
  }
}
