package com.web.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
	Member findByEmail(String email);
	Member findByNickname(String nickname);
	Boolean existsByEmail(String email);
	Boolean existsByNickname(String nickname);
	
}
