package com.example.malls.domain.service;

import com.example.malls.domain.dto.MemberJoinDTO;
import com.example.malls.domain.entity.Member;
import com.example.malls.domain.entity.MemberRole;
import com.example.malls.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService{

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        String mid = memberJoinDTO.getMid();
        boolean exist = memberRepository.existsById(mid);

        if(exist){
            throw new MidExistException();
        }

        Member member = dtoToMember(memberJoinDTO);
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        member.addRole(MemberRole.USER);

        log.info(" ======================= ");
        log.info(member);
        log.info(member.getRoleSet());
        log.info(" ======================= ");

        memberRepository.save(member);
    }
}
