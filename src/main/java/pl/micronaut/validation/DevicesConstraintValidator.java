package pl.micronaut.validation;

import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.validation.validator.constraints.ConstraintValidator;
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext;
import jakarta.inject.Singleton;
import pl.micronaut.configuration.ApplicationProperties.Device;

import java.util.List;

@Singleton
public class DevicesConstraintValidator
    implements ConstraintValidator<DevicesConstraint, List<Device>> {

  @Override
  public boolean isValid(
      List<Device> devices,
      AnnotationValue<DevicesConstraint> annotationMetadata,
      ConstraintValidatorContext context) {
    return true;
  }
}
