package pl.micronaut.controller;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.micronaut.configuration.ApplicationProperties;
import pl.micronaut.model.State;
import pl.micronaut.model.request.SceneRequest;
import pl.micronaut.service.StateService;

import javax.validation.Valid;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class StateController {

  private final StateService stateService;

  @Get("/config")
  public ApplicationProperties getApplicationProperties() {
    return stateService.getConfig();
  }

  @Get("/state")
  public State getState() {
    return stateService.getState();
  }

  @Get("/state/voltage")
  public Map<String, Integer> getStateVoltage() {
    return stateService.getStateVolatge();
  }

  @Get("/state/battery")
  public Map<String, Integer> getStateBattery() {
    return stateService.getStateBattery();
  }

  @Post("/scene")
  public ApplicationProperties sceneState(@Valid @Body SceneRequest sceneRequest) {
    stateService.setSceneState(sceneRequest);
    return stateService.getConfig();
  }
}
