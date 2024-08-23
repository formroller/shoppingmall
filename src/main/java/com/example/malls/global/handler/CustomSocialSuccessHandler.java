package com.example.malls.global.handler;

import com.example.malls.domain.dto.MemberSecurityDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class CustomSocialSuccessHandler implements AuthenticationSuccessHandler {
    /*소셜 로그인 성공 후 사용자 정보 수정 or 특정 페이지로 이동*/
    private final PasswordEncoder passwordEncoder;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("+++++++++++++++++++++++++++++++++++++++++++");
        log.info("Authentication - principal : \n" + authentication.getPrincipal());

        MemberSecurityDTO memberSecurityDTO = (MemberSecurityDTO) authentication.getPrincipal();
        log.info(memberSecurityDTO);

        String encodedPw = memberSecurityDTO.getMpw();
        log.info("pwd : " +encodedPw);

        // 소셜 로그인 and 패스워드 1111
        if (memberSecurityDTO.isSocial()
                && (memberSecurityDTO.getMpw().equals("1111")
                    ||  passwordEncoder.matches("1111", memberSecurityDTO.getMpw())
                )){


            log.info("Should Change Password");

            log.info("Redirect to Member Modify");

            response.sendRedirect("/member/modify");

        }else{
            response.sendRedirect("/board/list");
        }
    }

}
