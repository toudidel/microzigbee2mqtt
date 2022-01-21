package pl.micronaut.service.impl;

import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import pl.micronaut.configuration.ApplicationProperties;
import pl.micronaut.configuration.ApplicationProperties.Trigger;
import pl.micronaut.model.Message;
import pl.micronaut.model.PublisherMessage;
import pl.micronaut.model.request.SceneRequest;
import pl.micronaut.service.CoordinatorService;
import pl.micronaut.service.PublisherService;
import pl.micronaut.service.StateService;
import pl.micronaut.util.StringCommonUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class CoordinatorServiceImpl implements CoordinatorService {

  private final ApplicationProperties properties;
  private final PublisherService publisherService;
  private final StateService stateService;

  @Override
  public void process(String topic, Message message) {
    String device = topic.replaceFirst(properties.getQueuePrefix(), "");

    if (properties.getDevices().stream().noneMatch(d -> d.getName().equals(device))) {
      return; // ignore devices not listed in properties
    }

    String messageAsString = StringCommonUtils.objectToString(message.toString(), true);
    List<String> messageItems = Arrays.asList(StringUtils.leftPad(device, 12), messageAsString);

    stateService.saveState(device, message);

    log.info(
        messageItems.stream()
            .filter(s -> StringUtils.isNotBlank(s))
            .collect(Collectors.joining(" | ")));

    properties.getScenes().stream()
        .filter(scene -> BooleanUtils.isNotFalse(scene.getEnabled()))
        .filter(scene -> Objects.nonNull(scene.getDevice()))
        .filter(
            scene ->
                CollectionUtils.isNotEmpty(scene.getTriggers())
                    && scene.getDevice().equals(device)
                    && message.matches(scene.getProperty(), scene.getValue()))
        .forEach(
            scene -> processScene(scene.getName(), scene.getNotification(), scene.getTriggers()));
  }

  /**
   * Processes one scene.
   *
   * @param scene name of scene
   * @param notification notification settings
   * @param triggers list of triggers
   */
  private void processScene(
      String scene, ApplicationProperties.Notification notification, List<Trigger> triggers) {

    // loop for devices
    triggers.stream()
        .filter(trigger -> StringUtils.isNotBlank(trigger.getDevice()))
        .forEach(
            trigger ->
                publisherService.send(
                    PublisherMessage.builder()
                        .topic(properties.getQueuePrefix() + trigger.getDevice())
                        .notification(notification)
                        .message(
                            Message.builder()
                                .scene(scene)
                                .propertyValue(trigger.getProperty(), trigger.getValue())
                                .build())
                        .build()));

    // loop for ignore disable scenes
    triggers.stream()
        .filter(trigger -> StringUtils.isNotBlank(trigger.getDoNotDisable()))
        .forEach(trigger -> stateService.doNotDisable(trigger.getDoNotDisable()));

    // loop for disable scenes
    triggers.stream()
        .filter(trigger -> StringUtils.isNotBlank(trigger.getDisable()))
        .forEach(
            trigger ->
                stateService.setSceneState(
                    SceneRequest.builder().name(trigger.getDisable()).enabled(false).build()));

    // loop for enable scenes
    triggers.stream()
        .filter(trigger -> StringUtils.isNotBlank(trigger.getEnable()))
        .forEach(
            trigger ->
                stateService.setSceneState(
                    SceneRequest.builder().name(trigger.getEnable()).enabled(true).build()));
  }
}
