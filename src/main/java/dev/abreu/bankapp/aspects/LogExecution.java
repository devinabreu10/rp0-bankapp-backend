package dev.abreu.bankapp.aspects;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to log the execution time of a method. (Used for debugging purposes)
 * <p>
 * This annotation can be applied to any method to log its execution time at the DEBUG level.
 * The log message will include the name of the method and the time taken to execute
 * it in milliseconds.
 *
 * @author Devin Abreu
 * @since 0.0.1
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface LogExecution {
}
