package pl.micronaut.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = {DevicesConstraintValidator.class})
public @interface DevicesConstraint {

  String message() default "devices constraints violated ({validatedValue})";
}
