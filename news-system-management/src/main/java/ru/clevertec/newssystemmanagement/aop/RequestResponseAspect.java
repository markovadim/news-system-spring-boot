package ru.clevertec.newssystemmanagement.aop;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class RequestResponseAspect {

    @Around("execution(* ru.clevertec.newssystemmanagement.controllers.*.*(..))")
    public Object inspectController(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<?> response = (ResponseEntity<?>) joinPoint.proceed();
        log.info("Request Arguments: " + Arrays.toString(joinPoint.getArgs()));
        log.info(response.getStatusCode().toString());
        log.info(response.getBody().toString());
        return response;
    }
}
