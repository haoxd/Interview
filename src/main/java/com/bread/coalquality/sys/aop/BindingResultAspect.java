package com.bread.coalquality.sys.aop;


import com.bread.coalquality.sys.common.RespInfo;
import com.bread.coalquality.sys.constans.Constants;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
@Order(-100)
public class BindingResultAspect {

    @Pointcut("@annotation(com.bread.coalquality.sys.annotation.BindingError)")
    public void BindingResult() {
    }

    @Around("BindingResult()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {

        StringBuilder errorMessage = new StringBuilder();

        Arrays.asList(joinPoint.getArgs()).stream()
                .filter(arg->arg instanceof BindingResult )
                .map(arg->(BindingResult) arg)
                .filter(result->result.hasErrors()).map(error-> error.getAllErrors()).distinct().collect(Collectors.toList())
                .forEach(errorList->errorList.forEach(error->errorMessage.append("|").append(error.getDefaultMessage())));

        return errorMessage.length()>0 ?RespInfo.fail(errorMessage.toString()):joinPoint.proceed();
    }
}
