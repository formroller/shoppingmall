package com.example.malls.global.util;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class JWTUtilTest {

    @Autowired
    private JWTUtil jwtUtil;

    @DisplayName("키 생성")
    @Test
    public void testGenerated(){
//        log.info(key);
        Map<String, Object> claimMap = Map.of("mid", "abcde");

        String jwtStr = jwtUtil.generateToken(claimMap, 1);

        log.info(jwtStr);
    }

    @DisplayName("JWT 유효성 검증")
    @Test
    public void testValidated(){

        // 유효 시간 지난 토큰
        String jwtStr = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE3MjQzOTk4MjgsIm1pZCI6ImFiY2RlIiwiaWF0IjoxNzI0Mzk5NzY4fQ.wIgxYHIz_qq8L7Awy9Mp2z2Datvv1KWHYnCoP9bnbfw";

        Map<String , Object> claims = jwtUtil.validateToken(jwtStr);

        log.info(claims);
    }

    @DisplayName("JWT 문자열 생성 및 검증")
    @Test
    public void testAll(){
        String jwtStr = jwtUtil.generateToken(Map.of("mid", "AAAA", "email","aa@bb.com"),1);

        log.info(jwtStr);

        Map<String, Object> claims = jwtUtil.validateToken(jwtStr);

        log.info("MID: "+claims.get("mid"));
        log.info("EMAIL : "+claims.get("email"));
    }

}