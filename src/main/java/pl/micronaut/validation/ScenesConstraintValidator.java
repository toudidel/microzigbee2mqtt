package pl.micronaut.validation;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import org.apache.commons.lang3.StringUtils;
import pl.micronaut.configuration.ApplicationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Singleton
public class ScenesConstraintValidator
    implements ConstraintValidator<ScenesConstraint, List<ApplicationProperties.Scene>> {

  @Override
  public boolean isValid(
      List<ApplicationProperties.Scene> scenes,
      AnnotationValue<ScenesConstraint> annotationMetadata,
      ConstraintValidatorContext context) {

    // check if only one property is set
    boolean onlyOnePropertyCondition =
        scenes.stream()
            .noneMatch(
                s ->
                    Arrays.asList(s.getDevice(), s.getCron()).stream()
                            .filter(Objects::nonNull)
                            .count()
                        != 1);

    // check if property & value for device
    boolean propertyValueForDeviceCondition =
        scenes.stream()
            .filter(s -> StringUtils.isNotBlank(s.getDevice()))
            .allMatch(
                s ->
                    StringUtils.isNotBlank(s.getProperty())
                        && StringUtils.isNotBlank(s.getValue()));

    return onlyOnePropertyCondition && propertyValueForDeviceCondition;
  }
}
