package com.example.malls.global.config;

import com.example.malls.global.APILoginFilter;
import com.example.malls.global.handler.APILoginSuccessHandler;
import com.example.malls.global.handler.Custom403Handler;
import com.example.malls.global.security.CustomUserDetailsService;
import com.example.malls.global.handler.CustomSocialSuccessHandler;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;


@Configuration
@Log4j2
@RequiredArgsConstructor
@EnableMethodSecurity(prePostEnabled = true) // 권한 설정
public class CustomSecurityConfig {

    private final DataSource dataSource; // 자동 로그인 - 쿠키와 관련된 정보를 테이블로 보관
    private final CustomUserDetailsService userDetailsService;


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        log.info(" -------- Configure ------ ");

        // todo 추가
        /* AuthenticationManager 설정 */
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                        .passwordEncoder(passwordEncoder());

        // Get AuthenticationManager
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();

        // APILoginFilter
        APILoginFilter apiLoginFilter = new APILoginFilter("/generateToken");
        apiLoginFilter.setAuthenticationManager(authenticationManager);

        // APILoginSuccessHandler
        APILoginSuccessHandler apiLoginSuccessHandler = new APILoginSuccessHandler();
        // SuccessHandler 세팅
        apiLoginFilter.setAuthenticationSuccessHandler(apiLoginSuccessHandler);

        http
                // todo (추가1) authentication 설정
                .authenticationManager(authenticationManager)
                .addFilterBefore(apiLoginFilter, UsernamePasswordAuthenticationFilter.class)

                // CSRF 비활성화
                .csrf(AbstractHttpConfigurer::disable)
                 // 세션 미사용 - APILogin 적용시
//                .sessionManagement(sessionConfig ->
//                sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                        )
                // 인증 처리
                .authorizeHttpRequests(request -> request
                        .dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
                        .anyRequest().authenticated() // 어떤 요청도 인증 필요
                )
                .formLogin(login -> login
                        .loginPage("/member/login") // 커스텀 로그인 페이지
                        .defaultSuccessUrl("/board/list", true) // 성공시 dashboard
                        .permitAll()
                )
                // Remember Me 설정
                .rememberMe(customizer -> customizer
                        .key("12345678")
                        .tokenRepository(persistentTokenRepository())
                        .userDetailsService(userDetailsService)
                        .tokenValiditySeconds(60*60*24*30)
                )
                .logout(Customizer.withDefaults()) // 로그아웃 기본 설정
                .exceptionHandling(handler ->
                        handler.accessDeniedHandler(accessDeniedHandler())) // 403 에러 처리
                // OAuth2 로그인 설정
                .oauth2Login(oauth -> oauth
                        .loginPage("/member/login")
                        .successHandler(authenticationSuccessHandler())
//                        .defaultSuccessUrl("/board/list", true)
                )

                ;

        return http.build();
    }


    // 정적 자원 처리
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        log.info(" ---- web configure -----");

        return (web) -> web.ignoring().requestMatchers(
                PathRequest.toStaticResources().atCommonLocations()
        );
    }

    //쿠키 생성시 쿠키 값 인코딩하기 위한 키값과 필요한 정보 저장
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    // 403 예외 처리
    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new Custom403Handler();
}

    // 소셜 로그인 처리
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler(){
        return new CustomSocialSuccessHandler(passwordEncoder());
    }

}




