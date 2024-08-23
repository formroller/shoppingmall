package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Member;
import com.example.malls.domain.entity.MemberRole;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Commit;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Log4j2
class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .mid("member"+i)
                    .mpw(passwordEncoder.encode("1111"))
                    .email("email"+i+"@aaa.bb")
                    .build();

            member.addRole(MemberRole.USER);

            if(i>=90){
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }

    @DisplayName("회원 조회 테스트")
    @Test
    public void testRead(){
        Optional<Member> result = memberRepository.getWithRoles("member100");

        Member member = result.orElseThrow();

        log.info(member);
        log.info(member.getRoleSet());

        member.getRoleSet().forEach(memberRole -> log.info(memberRole.name()));
    }

    @Commit
    @Test
    void updatePassword() {

//        String mid = "dldydwns10@gmail.com"; // 소셜 로그인으로 추가된 사용자로 현재 DB 존재 이메일
        String mid = "lyj0094@naver.com"; // 소셜 로그인으로 추가된 사용자로 현재 DB 존재 이메일

        log.info("Before Update : "+ memberRepository.findByEmail(mid));

        String newEncodedPassword = passwordEncoder.encode("alpha");
        memberRepository.updatePassword(newEncodedPassword, mid);

        // 영속성 컨텍스트 초기화해 DB에서 값 다시 읽음
        entityManager.clear();
        Member updateMember = memberRepository.findByEmail(mid).orElseThrow();
        log.info("After Update : "+updateMember);


        // 패스워드 업데이트 검증
        boolean isPasswordMatched = passwordEncoder.matches("1111", updateMember.getMpw());
        log.info("Password Matched : "+ isPasswordMatched);

    }


    // JWT 가입 유저 생성
    @DisplayName("JWT 가입 유저 생성")
    @Test
    public void testApiUserInput(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .mid("ApiUser"+i)
                    .email("user"+i+"@aa.com")
                    .mpw(passwordEncoder.encode("1111"))
                    .social(false)
                    .del(false)
                    .build();

            member.addRole(MemberRole.USER);

            memberRepository.save(member);
        });
    }

    @Test
    public void removeUser(){
        IntStream.rangeClosed(1,100).forEach(i->{
            memberRepository.deleteById("ApiUser"+i);}
            );
    }
}