package pl.micronaut.validation;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({FIELD})
@Constraint(validatedBy = {ScenesConstraintValidator.class})
public @interface ScenesConstraint {

  String message() default "scenes constraints violated ({validatedValue})";
}
