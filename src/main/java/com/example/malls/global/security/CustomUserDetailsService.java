package com.example.malls.global.security;

import com.example.malls.domain.dto.MemberSecurityDTO;
import com.example.malls.domain.entity.Member;
import com.example.malls.domain.repository.MemberRepository;
import com.example.malls.global.config.CustomSecurityConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("loadByUserName : " +username);


        Optional<Member> result = memberRepository.getWithRoles(username);

        if(result.isEmpty()){ // 해당 사용자 없다면
            throw new UsernameNotFoundException("Username Not Found");
        }

        Member member = result.get();

        MemberSecurityDTO memberSecurityDTO =
                new MemberSecurityDTO(
                        member.getMid(),
                        member.getMpw(),
                        member.getEmail(),
                        member.isDel(),
                        false,
                        member.getRoleSet()
                                .stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_"+memberRole.name()))
                                .collect(Collectors.toList())
                );

        log.info("memberSecurityDTO");
        log.info(memberSecurityDTO);

        return memberSecurityDTO;
    }
}
