package com.example.malls.global;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

@Log4j2
public class APILoginFilter extends AbstractAuthenticationProcessingFilter {

    /* 인증 단계 처리 및 인증 성공시 토큰 전송*/

    public APILoginFilter(String defaultFilterProcessUrl){
        super(defaultFilterProcessUrl);
    }
    
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {

        log.info("----------------- ApiLoginFilter -----------------");

        if(request.getMethod().equals("GET")){
            log.info("GET METHOD NOT SUPPORT");
            return null;
        }

        Map<String , String > jsonData = parseRequestJSON(request);

        log.info("============ jsonData : "+ jsonData+" ================");

        // Token 인증 정보 생성
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(
                        jsonData.get("mid"),
                        jsonData.get("mpw")
        );

        // 이후 인증 처리는 UsernamePasswordAuthenticationFilter에서 하도록 구성
        // todo - 알라딘 -> 토큰 구성 / goodnote -> 적용 방법
        return getAuthenticationManager().authenticate(authenticationToken);

//        return null;
    }

    private Map<String, String> parseRequestJSON(HttpServletRequest request){
        // JSON 데이터 분석해 mid, mpw 값을 Map 처리
        try(Reader reader = new InputStreamReader(request.getInputStream())){
            Gson gson = new Gson();

            return gson.fromJson(reader, Map.class);

        }catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    
}
