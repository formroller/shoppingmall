package com.example.malls.domain.repository;

import com.example.malls.domain.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {
    @EntityGraph(attributePaths = "roleSet")
    @Query("select m from Member m where m.mid=:mid and m.social = false")
    Optional<Member> getWithRoles(String mid);

    // 소셜 계정 여부로 사용자 이메일 확인
    @EntityGraph(attributePaths = "roleSet")
    Optional<Member> findByEmail(String email);

    // 소셜 사용자 정보 수정
    @Modifying
    @Transactional
    @Query("update Member m set m.mpw =:mpw where m.mid =:mid ")
    void updatePassword(@Param("mpw") String password, @Param("mid") String mid);
}
