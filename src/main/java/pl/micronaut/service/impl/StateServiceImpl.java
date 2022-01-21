package pl.micronaut.service.impl;

import io.micronaut.core.util.CollectionUtils;
import jakarta.inject.Singleton;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import pl.micronaut.configuration.ApplicationProperties;
import pl.micronaut.model.DoNotDisable;
import pl.micronaut.model.Message;
import pl.micronaut.model.State;
import pl.micronaut.model.State.DeviceMessage;
import pl.micronaut.model.request.SceneRequest;
import pl.micronaut.service.StateService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Singleton
@RequiredArgsConstructor
public class StateServiceImpl implements StateService {

  private static State STATE;

  // list of scenes that shouldn't be disabled in nested triggers
  private static List<DoNotDisable> doNotDisableScenes;
  // number of seconds when the added scenes should exist in the list
  private final int DO_NOT_DISABLE_TIME = 1;

  private final ApplicationProperties properties;

  @Override
  public ApplicationProperties getConfig() {
    return properties;
  }

  @Override
  public State getState() {
    if (STATE == null) {
      STATE = State.builder().deviceMessages(new HashMap<>()).build();
    }
    return STATE;
  }

  @Override
  public void saveState(String device, Message message) {
    LocalDateTime now = LocalDateTime.now();

    State state = getState();
    state.setLastModifyDate(now.toString());
    state.setLastModifyDevice(device);
    state
        .getDeviceMessages()
        .put(device, DeviceMessage.builder().modifyDate(now.toString()).message(message).build());
  }

  @Override
  public Map<String, Integer> getStateVolatge() {
    return STATE.getDeviceMessages().entrySet().stream()
        .filter(e -> Objects.nonNull(e.getValue().getMessage().getVoltage()))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getMessage().getVoltage()));
  }

  @Override
  public Map<String, Integer> getStateBattery() {
    return STATE.getDeviceMessages().entrySet().stream()
        .filter(e -> Objects.nonNull(e.getValue().getMessage().getBattery()))
        .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().getMessage().getBattery()));
  }

  @Override
  public void setSceneState(SceneRequest sceneRequest) {
    // check if scene is in the list of scenes that shouldn't be disabled
    if (BooleanUtils.isFalse(sceneRequest.getEnabled())
        && getDoNotDisableScenes().stream()
            .anyMatch(s -> s.getScene().equals(sceneRequest.getName()))) {

      removeDoNotDisableScene(sceneRequest.getName());
      log.info("Do not disable: " + sceneRequest.getName());
      return;
    }

    properties.getScenes().stream()
        .filter(scene -> scene.getName().equals(sceneRequest.getName()))
        .forEach(
            scene -> {
              scene.setEnabled(sceneRequest.getEnabled());
              log.info(
                  String.format(
                      "Scene: %s | State: %s", sceneRequest.getName(), sceneRequest.getEnabled()));
            });
  }

  @Override
  public void doNotDisable(String scene) {
    getDoNotDisableScenes()
        .add(DoNotDisable.builder().scene(scene).createdAt(LocalDateTime.now()).build());
  }

  private List<DoNotDisable> getDoNotDisableScenes() {
    if (CollectionUtils.isEmpty(doNotDisableScenes)) {
      doNotDisableScenes = new ArrayList<>();
    }
    return doNotDisableScenes;
  }

  /**
   * Removes scene from list Do Not Disable when older than DO_NOT_DISABLE_TIME. Some time is needed
   * because of duplicated messages in a row.
   *
   * @param scene scene
   */
  private void removeDoNotDisableScene(String scene) {
    getDoNotDisableScenes()
        .removeIf(
            s ->
                s.getScene().equals(scene)
                    && ChronoUnit.SECONDS.between(s.getCreatedAt(), LocalDateTime.now())
                        >= DO_NOT_DISABLE_TIME);
  }
}
