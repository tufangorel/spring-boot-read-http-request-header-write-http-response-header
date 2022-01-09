package com.company.customerinfo.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class PerformanceAspect {

    private Logger LOGGER = LoggerFactory.getLogger(PerformanceAspect.class);

    @Around("execution(* com.company.customerinfo.repository.*.*(..))()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {

        long start = System.nanoTime();
        Object retval = pjp.proceed();
        long end = System.nanoTime();
        String methodName = pjp.getSignature().getName();
        LOGGER.info("Execution of " + methodName + " took " + TimeUnit.NANOSECONDS.toMillis(end - start) + " ms");
        return retval;
    }

}