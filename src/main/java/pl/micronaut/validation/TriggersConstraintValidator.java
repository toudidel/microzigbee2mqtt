package pl.micronaut.validation;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import pl.micronaut.configuration.ApplicationProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Singleton
public class TriggersConstraintValidator
    implements ConstraintValidator<TriggersConstraint, List<ApplicationProperties.Trigger>> {

  @Override
  public boolean isValid(
      List<ApplicationProperties.Trigger> triggers,
      AnnotationValue<TriggersConstraint> annotationMetadata,
      ConstraintValidatorContext context) {

    // check if only one property is set
    return triggers.stream()
        .noneMatch(
            t ->
                Arrays.asList(t.getDevice(), t.getDisable(), t.getEnable(), t.getDoNotDisable())
                        .stream()
                        .filter(Objects::nonNull)
                        .count()
                    != 1);
  }
}
