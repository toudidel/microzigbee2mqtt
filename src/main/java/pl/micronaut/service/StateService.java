package pl.micronaut.service;

import pl.micronaut.configuration.ApplicationProperties;
import pl.micronaut.model.Message;
import pl.micronaut.model.State;
import pl.micronaut.model.request.SceneRequest;

import java.util.Map;

/** State service. */
public interface StateService {

  /**
   * Return application properties.
   *
   * @return application properties
   */
  ApplicationProperties getConfig();

  /**
   * Returns state of all devices.
   *
   * @return state
   */
  State getState();

  /**
   * Saves state of devices using most recent message.
   *
   * @param device device
   * @param message message
   */
  void saveState(String device, Message message);

  /**
   * Returns map with voltage values for all devices have property voltage.
   *
   * @return map
   */
  Map<String, Integer> getStateVolatge();

  /**
   * Returns map with battery values for all devices have property battery.
   *
   * @return map
   */
  Map<String, Integer> getStateBattery();

  /**
   * Disables/enables scene.
   *
   * @param sceneRequest scene request
   */
  void setSceneState(SceneRequest sceneRequest);

  /**
   * Adds scene to list of scenes that shouldn't be disabled by nested triggers.
   *
   * @param scene scene
   */
  void doNotDisable(String scene);
}
