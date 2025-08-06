package com.beyond.board.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Aspect // AOP 관련 코드임을 명시
@Component
@Slf4j
public class AopLogService {

    // aop의 대상(공통작업)이 되는 controller, service 등의 위치를 명시
    // 아래 위치, Controller 어노테이션이 붙는 컨트롤러 앞단에서 끼어들겠다. (특정 컨트롤러도 지정할 수 있음)
    @Pointcut("within(@org.springframework.stereotype.Controller *)")
    public void controllerPointCut() {

    }


//    // controller의 진입하기 전에 사용자 요청을 log출력. 핵심로직 수행 후 log출력
//    // 방법1. around 어노테이션을 통해 controller에 걸처져 있는 코드 패턴 사용
//    @Around("controllerPointCut()")
//    public Object controllerLogger(ProceedingJoinPoint joinPoint) throws Throwable {
//        // joinPoint는 사용자가 실행하려고 하는 코드를 의미하고, 위에서 정의한 pointcut을 의미
//        log.info("aop start");
//        log.info("method 명: " + joinPoint.getSignature().getName());
//
//        // 직접 HttpServletRequest객체에서 사용자 요청 정보 추출
//        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = attributes.getRequest();
//        log.info("HTTP 메서드 : " + request.getMethod());
//
//        Map<String, String[]> parameterMap = request.getParameterMap();
//        ObjectMapper objectMapper = new ObjectMapper();
//        ObjectNode objectNode = objectMapper.valueToTree(parameterMap);
//        log.info("사용자 input 값 : " + objectNode);
//
//        // 대상이 되는 controller 로직 수행
//        Object object = joinPoint.proceed();
//
//
//        log.info("aop end");
//
//        return object;
//    }



    // 방법2. Before, After어노테이션 사용
    @Before("controllerPointCut()")
    public void beforeController(JoinPoint joinPoint) {
        log.info("aop start");
        log.info("method 명: " + joinPoint.getSignature().getName());

        // 직접 HttpServletRequest객체에서 사용자 요청 정보 추출
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        log.info("HTTP 메서드 : " + request.getMethod());

        Map<String, String[]> parameterMap = request.getParameterMap();
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.valueToTree(parameterMap);
        log.info("사용자 input 값 : " + objectNode);
    }

    @After("controllerPointCut()")
    public void afterController() {
        log.info("aop end");
    }

}
