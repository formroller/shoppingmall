package com.example.pracboard.domain.member.repository;

import com.example.pracboard.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
