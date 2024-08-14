package com.example.malls.domain.service;

import com.example.malls.domain.dto.MemberJoinDTO;
import com.example.malls.domain.entity.Member;

public interface MemberService {
    static class MidExistException extends Exception{}

    void join(MemberJoinDTO memberJoinDTO) throws MidExistException;

    /* 직렬화 역직렬화 */
    default Member dtoToMember(MemberJoinDTO memberJoinDTO){
        Member member = Member.builder()
                .mid(memberJoinDTO.getMid())
                .mpw(memberJoinDTO.getMpw())
                .email(memberJoinDTO.getEmail())
                .del(memberJoinDTO.isDel())
                .social(memberJoinDTO.isSocial())
                .build();

        return member;
    }

    default MemberJoinDTO memberToDTO(Member member){
        MemberJoinDTO joinDTO = MemberJoinDTO.builder()
                .mid(member.getMid())
                .mpw(member.getMpw())
                .email(member.getEmail())
                .del(member.isDel())
                .social(member.isSocial())
                .build();

        return joinDTO;
    }

}
