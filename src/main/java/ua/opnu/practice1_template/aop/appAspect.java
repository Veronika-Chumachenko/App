package ua.opnu.practice1_template.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class appAspect {

    //логування назви методу, його параметрів та час виконання.
    @Around("execution(* ua.opnu.practice1_template.controller.*.*(..)) || execution(* ua.opnu.practice1_template.service.*.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        System.out.println("Виклик: " + joinPoint.getSignature() + " з параметрами: " + Arrays.toString(joinPoint.getArgs()));
       Object result = joinPoint.proceed();
        long duration = System.currentTimeMillis() - start;
        System.out.println("Метод " + joinPoint.getSignature() + " виконано за " + duration + " мс");

        return result;
    }


    //аудіт імен користувачів і методів, що вони викликають.
    @After("execution(* ua.opnu.practice1_template.controller.*.*(..))")
    public void auditAction(JoinPoint joinPoint) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        System.out.println("Користувач: " + username + " викликав " + joinPoint.getSignature());
    }


    @AfterThrowing(pointcut = "execution(* ua.opnu.practice1_template.*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        System.err.println("Помилка у методі: " + joinPoint.getSignature() + " — " + ex.getMessage());
    }


    @Around("@annotation(ua.opnu.practice1_template.security.AdminOnly)")
    public Object checkAdmin(ProceedingJoinPoint joinPoint) throws Throwable {
        String role = SecurityContextHolder.getContext().getAuthentication().getAuthorities().toString();
        if (!role.contains("ADMIN")) {
            throw new AccessDeniedException("Доступ заборонено!");
        }
        return joinPoint.proceed();
    }

}