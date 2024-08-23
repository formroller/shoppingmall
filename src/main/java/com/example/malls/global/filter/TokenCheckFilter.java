package com.example.malls.global.filter;

import com.example.malls.global.security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RequiredArgsConstructor
public class TokenCheckFilter {
    private final CustomUserDetailsService customUserDetailsService;
//    private final JwtUtil;
}
