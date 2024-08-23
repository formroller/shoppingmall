package com.example.malls.global.handler;

import com.example.malls.global.util.JWTUtil;
import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
public class APILoginSuccessHandler implements AuthenticationSuccessHandler {
    /* LoginSuccessHandler - 인증 성공 후 처리 담당*/

    private final JWTUtil jwtUtil;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("============= Login Success Handler =============");

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        log.info(authentication);
        log.info(authentication.getName()); // Username


        Map<String, Object> claims = Map.of("mid", authentication.getName());

        // AccessToken - 유효기간 1일
        String accessToken = jwtUtil.generateToken(claims, 1);

        // RefreshToken - 유효기간 30일
        String refreshToken = jwtUtil.generateToken(claims, 30);

        Gson gson = new Gson();


        Map<String, Object> keyMap = Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        );

        String jsonStr = gson.toJson(keyMap);

        response.getWriter().println(jsonStr);


    }
}
