package com.example.malls.global.filter;

import com.example.malls.global.advice.exception.AccessTokenException;
import com.example.malls.global.security.CustomUserDetailsService;
import com.example.malls.global.util.JWTUtil;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter extends OncePerRequestFilter {
    /* 현재 사용자가 로그인한 사용자인지 체크하는 필터
    * OncePerRequestFilter - 한 요청에 한번씩 동작하는 필터
    */

    private final JWTUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        if(!path.startsWith("/board/")){
            filterChain.doFilter(request, response);
            return;
        }

        log.info(" ============ Token Check Filter ========== ");
        log.info(" JWTUtil : "+jwtUtil);

        filterChain.doFilter(request, response);

    }

    // AccessToken 검증 및 예외 종류에 따라 Exception 처리
    private Map<String, Object> validateAccessToken(HttpServletRequest request) throws AccessTokenException{

        String headerStr = request.getHeader("Authorization");

        if(headerStr == null || headerStr.length()<8){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.UNACCEPT);
        }

        // Bearer 생략
        String tokenType = headerStr.substring(0,6);
        String tokenStr = headerStr.substring(7);

        if(tokenType.equalsIgnoreCase("Bearer") == false){
            throw new AccessTokenException(AccessTokenException.TOKEN_ERROR.BADTYPE);
        }

        try{
            Map<String, Object> values = jwtUtil.validateToken(tokenStr);

            return values;

        }catch (MalformedJwtException malformedJwtException)
    }

}
