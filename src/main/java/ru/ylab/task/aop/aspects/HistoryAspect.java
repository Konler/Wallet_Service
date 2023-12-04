package ru.ylab.task.aop.aspects;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import ru.ylab.task.service.PlayerService;

import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class HistoryAspect {

    private final PlayerService playerService;

    @Pointcut("execution(* ru.ylab.task.controller..*.*(..))")
    public void controllerMethods() {}

    @After("controllerMethods()")
    public void logServletMethodExecution(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getName();
        if (!Objects.equals(methodName, "init")) {
            HttpSession session = (HttpSession) joinPoint.getArgs()[0];
            Long id = (Long) session.getAttribute("id");
            if (id != null) {
                playerService.addActionToHistory(id, methodName);
            }
        }
    }

}
