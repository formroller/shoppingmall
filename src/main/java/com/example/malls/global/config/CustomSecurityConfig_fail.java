//package com.example.malls.global.config;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.log4j.Log4j2;
//import org.springframework.context.annotation.Configuration;
//
//@Log4j2
//@Configuration
//@RequiredArgsConstructor
//public class CustomSecurityConfig_fail {
//
//    // 시큐리티 설정 추가
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
////
////        log.info(" ------ configure -------");
////
////        http
////                .authorizeHttpRequests(authorizeRequest->
////                        authorizeRequest
////                                .requestMatchers("/static/**").permitAll()
////                                .anyRequest().authenticated());
////
////        return http.build();
////    }
//
//
//    // 정적 자원 처리
////    @Bean
////    public WebSecurityCustomizer webSecurityCustomizer() {
////        log.info(" ------------ web configure -------------");
////
////        return (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations())
////                .requestMatchers("/css/**", "/js/**", "/images/**");
////    }
//}
