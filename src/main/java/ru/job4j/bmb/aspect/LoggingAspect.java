package ru.job4j.bmb.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {

    @Pointcut("execution(* ru.job4j.bmb.services.*.*(..))")
    private void serviceLayerPointCut() {}

    @Before("serviceLayerPointCut()")
    public void beforeServiceLayer(JoinPoint joinPoint) {
        System.out.println("Вызов метода: " + joinPoint.getSignature().getName());
        System.out.println("Параметры: " + Arrays.toString(joinPoint.getArgs()));
    }
}
