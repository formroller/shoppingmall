package com.example.malls.global.util;


import com.nimbusds.jose.jwk.JWKException;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@Log4j2
public class JWTUtil {
    /* JWT 생성 및 검증 역할*/

//    @Value("${com.example.malls.secret}")
    @Value("${jwt.secret}")
    private String key;

    // 키 생성
    public String generateToken(Map<String, Object> valueMap, int days){

        log.info("generateKey ::::::::: "+key);

        // 헤더
        Map<String, Object> headers = new HashMap<>();
        headers.put("typ", "JWT");
        headers.put("alg", "HS256");

        // payload
        Map<String, Object> payloads = new HashMap<>();
        payloads.putAll(valueMap);

        // 테스트시 짧은 유효기간
        int time = (1) * days; // 테스트는 분단위 (추후 60*24 일)

        String jwtStr = Jwts.builder()
                .setHeader(headers)
                .setClaims(payloads)
                .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(time).toInstant()))
                .signWith(SignatureAlgorithm.HS256, key.getBytes())
                .compact();

        return jwtStr;

    }

    // 키 검증
    public Map<String, Object> validateToken(String token) throws JwtException{

        Map<String, Object> claims = null;

        claims = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();


        return claims;
    }

}
