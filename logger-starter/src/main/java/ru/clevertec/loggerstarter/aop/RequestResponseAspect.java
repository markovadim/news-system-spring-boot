package ru.clevertec.loggerstarter.aop;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

@Aspect
public class RequestResponseAspect {

    private final Logger log = LoggerFactory.getLogger(RequestResponseAspect.class);

    @Pointcut("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void getMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    public void postMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void putMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void deleteMapping() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.PatchMapping)")
    public void patchMapping() {
    }

    @Around("getMapping() || postMapping() ||putMapping() || deleteMapping() || patchMapping()")
    public Object inspectController(ProceedingJoinPoint joinPoint) throws Throwable {
        ResponseEntity<?> response = (ResponseEntity<?>) joinPoint.proceed();
        log.info("Request Arguments: " + Arrays.toString(joinPoint.getArgs()));
        log.info(response.getStatusCode().toString());
        log.info(response.getBody().toString());
        return response;
    }
}
