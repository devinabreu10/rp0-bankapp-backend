package dev.abreu.bankapp.aspects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * Aspect class for debugging purposes.
 * <p>
 * This aspect logs at the DEBUG level.
 *
 * @author Devin Abreu
 * @since 0.0.1
 */
@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger = LogManager.getLogger(LoggingAspect.class);

    /**
     * Logs a debug message before the execution of a method in the controllers package.
     *
     * @param joinPoint the join point of the method execution
     */
    @Before("execution(* dev.abreu.bankapp.controllers.*.*(..))")
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.debug("Before: Entering {} method", methodName);
    }

    /**
     * Logs a debug message after the execution of a method in the controllers package.
     *
     * @param joinPoint the join point of the method execution
     */
    @After("execution(* dev.abreu.bankapp.controllers.*.*(..))")
    public void logAfter(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.debug("After: Exiting {} method", methodName);
    }

    /**
     * Logs the execution time of a method annotated with @LogExecution.
     * <p>
     * This advice measures the time taken by the method to execute and
     * logs it at the DEBUG level.
     *
     * @param pjp the proceeding join point of the method execution
     * @return the result of the method execution
     * @throws Throwable if an error occurs during method execution
     */
    @Around("@annotation(dev.abreu.bankapp.aspects.LogExecution)")
    public Object logExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        long startTime = System.nanoTime();

        Object result = pjp.proceed();

        long endTime = System.nanoTime();

        long executionTime = endTime - startTime;

        logger.debug("Execution of method {} took {} ms",
                pjp.getSignature().getName(), executionTime / 1000000);

        return result;
    }
}
