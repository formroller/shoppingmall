package com.example.malls.global.security;

import com.example.malls.domain.dto.MemberSecurityDTO;
import com.example.malls.domain.entity.Member;
import com.example.malls.domain.entity.MemberRole;
import com.example.malls.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.hibernate.validator.cfg.defs.UUIDDef;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomOauth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("\n ===== userRequest =======");
        log.info(userRequest);

        log.info("\n =========== OAuth2 User ===========");

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        String clientName = clientRegistration.getClientName();

        log.info("\n Client Name : " + clientName);

        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> paraMap = oAuth2User.getAttributes();


        String email = null;

        switch (clientName) {
            case "kakao" -> {
                email = getKakaoNickname(paraMap);

                return generateDTO(email, paraMap);
            }
            // todo google - email 추출
            case "google" -> {
                email = getGoogleEmail(paraMap);

                return generateDTO(email, paraMap);
            }
        }

        return null;
//        return memberSecurityDTO;
    }


    // 카카오 닉네임 추출 및 이메일 생성
    private String getKakaoNickname(Map<String, Object> paraMap) {
        log.info(" ============= Kakao ============= ");

        Object value = paraMap.get("kakao_account");

        log.info(value);

        LinkedHashMap profileMap = (LinkedHashMap) value; // 클라이언트내 profile 전체 정보 추출

        String email = (String) profileMap.get("email");

        log.info("email is : "+email);

        return email;

        /*이메일 없는 경우 임의 이메일 생성*/
//
//        LinkedHashMap profile = (LinkedHashMap) profileMap.get("profile"); // 계정 관련 정보 추출
//
//
//        // todo - profile에서 nickname 추출 후 return 하기
//        // 이메일 생성
//        if(profile.containsKey("nickname")){
////            String result = (String) profile.get("nickname");
//
//            String result = UUID.randomUUID()+"@"+"aaa.com";
//
//            return result;
//        }
//        return null;
//    return null;
    }

    // 구글 이메일 추출
    private String getGoogleEmail(Map<String, Object> paraMap){

        log.info(" ======= google ==========");

        String value = (String) paraMap.get("email");

        return value;
    }



    // 회원 생성
    private MemberSecurityDTO generateDTO(String email, Map<String, Object> params){

        Optional<Member> result = memberRepository.findByEmail(email);

        // * DB 내 해당 사용자 없을 경우
        if(result.isEmpty()){
            // 1. 회원 추가
            Member member = Member.builder()
                    .mid(email)
                    .mpw(passwordEncoder.encode("1111"))
                    .email(email)
                    .social(true)
                    .build();

            member.addRole(MemberRole.USER);
            memberRepository.save(member);

            // MemberSecurityDTO 구성 및 변환
            MemberSecurityDTO memberSecurityDTO = new MemberSecurityDTO(
                    email, "1111", email, false, true,
                    Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"))
            );

            memberSecurityDTO.setProps(params);

            return memberSecurityDTO;

        }else {
            Member member = result.get();

            MemberSecurityDTO memberSecurityDTO =
            new MemberSecurityDTO(
                    member.getMid(),
                    member.getMpw(),
                    member.getEmail(),
                    member.isDel(),
                    member.isSocial(),
                    member.getRoleSet()
                            .stream().map(memberRole ->
                            new SimpleGrantedAuthority("ROLE_"+memberRole.name())).collect(Collectors.toList())
            );
            return memberSecurityDTO;
        }
    }
}
