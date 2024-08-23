package com.example.malls.global.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;

@Log4j2
public class Custom403Handler implements AccessDeniedHandler {
    /* 403 발생 조건
    * 현재 사용자 권한 없는 경우
    * 특정 조건이 맞지 않는 경우
    ** 상황에 따른 메세지 처리
    * 1. <form> 태그 요청이 403인 경우, 로그인 페이지로 이동할 때 'ACCESS_DENIED' 값을 파라미터로 전달
    * 2. Ajax인 경우 JSON 데이터 만들어 전송
    */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {

        log.info(" --------------- ACCESS DENIED --------------- ");
        response.setStatus(HttpStatus.FORBIDDEN.value());

        // JSON 요청이었는지 확인
        String contentType = request.getHeader("Content-Type");

        boolean jsonRequest = contentType.startsWith("application/json");

        log.info("isJSON : "+jsonRequest);

        // 일반 request
        if(!jsonRequest){
            response.sendRedirect("/member/login?error=ACCESS_DENIED");
        }
    }

}
