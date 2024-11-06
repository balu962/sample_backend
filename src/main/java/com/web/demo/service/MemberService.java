package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.web.demo.config.MemberRole;
import com.web.demo.dto.MemberForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.entity.Board;
import com.web.demo.entity.Member;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.MemberRepository;


@Service
public class MemberService {
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	// 멤버 추가
	public void create(MemberForm memberForm) {
		Member member = new Member();
		member.setNickname(memberForm.getNickname());
		member.setEmail(memberForm.getEmail());
		member.setPassword(this.passwordEncoder.encode( memberForm.getPassword() ));
		member.setCreateDate(LocalDateTime.now());
		member.setMemberRole(MemberRole.BASIC);
		this.memberRepository.save(member);
}
	// 멤버 id(번호) 넣고 닉네임 찾기
	public String getNickname(Long memberId) {
		Optional<Member> oMember =this.memberRepository.findById(memberId);
		Member member = oMember.get();
		return member.getNickname();
	}
	// 비밀번호 기존 비밀번호랑 일치하는지 확인
    public boolean checkPassword(String email, String password) {
    	Member member = memberRepository.findByEmail(email);
    	// 비밀번호가 암호화돼있기 때문에 인코더 이용해서 비교 요청
        return passwordEncoder.matches(password, member.getPassword());
    }
    // 비밀번호 변경
    public void modifyPassword(String email, String newPassword) {
    	Member member = memberRepository.findByEmail(email);
    	// 비밀번호 암호화해서 저장
        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }
    // 닉네임 변경
	public boolean modifyNickname(String email,String nickname) {
		Member member = this.memberRepository.findByEmail(email);
		try {
			member.setNickname(nickname);
			this.memberRepository.save(member);
			// 닉네임 변경 후 사용자 정보 갱신을 위해 추가
		    UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		    // 사용자 정보 업데이트 (예: nickname 변경)
		    ((MemberUserDto) userDetails).setNickname(nickname);
		    // 새로운 인증 정보 생성
		    UsernamePasswordAuthenticationToken newAuth = new UsernamePasswordAuthenticationToken(
		        userDetails
		        ,null
		        ,userDetails.getAuthorities()
		    );

		    // SecurityContextHolder에 새로운 인증 정보 설정
		    SecurityContextHolder.getContext().setAuthentication(newAuth);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	// 중복 체크
	public Boolean checkEmailDuplication(String email) {
		return memberRepository.existsByEmail(email);
	}
	public Boolean checkNicknameDuplication(String nickname) {
		return memberRepository.existsByNickname(nickname);
	}
	// 멤버 리스트
    public void memberFormList(Model model) {
    	List<Member> memberList = this.memberRepository.findAll();
    	 model.addAttribute("memberList", 
    	 memberList.stream()
         .map(this::memberFormByMember)
         .collect(Collectors.toList())
         );
    }
    public MemberForm memberFormByMember(Member member) {
    	MemberForm memberForm = new MemberForm();
    	memberForm.setId(member.getId());
    	memberForm.setEmail(member.getEmail());
    	memberForm.setNickname(member.getNickname());
    	memberForm.setCreateDate(member.getCreateDate());
    	memberForm.setMemberRole(member.getMemberRole().getRole());
//    	memberForm.setMemberRole(member.getMemberRole());
    	return memberForm;
    }
    // 삭제
    public void delete(Long memberId) {
    	this.memberRepository.deleteById(memberId);
    }
    // 멤버 본인 또는 관리자인지 확인
    public boolean checkAccess(MemberUserDto userDetails, Member origMember ) {
    	Member member= this.getOneMember(userDetails.getMemberId());
        return member.getMemberRole() == MemberRole.ADMIN || member.equals(origMember);
    }
    public Member getOneMember(Long memberId) {
		return this.memberRepository.findById(memberId)
				.orElseThrow(() -> new DataNotFoundException("잘못된 유저 정보입니다"));
    }
    public void setRole(Long memberId, int roleNum) {
    	Member member = this.getOneMember(memberId);
//    	String role;
    	MemberRole memberRole;
    	switch (roleNum) {
        case 1:
//            role = "ROLE_ADMIN";
        	memberRole = MemberRole.ADMIN;
            break;
        case 2:
//            role = "ROLE_SUBADMIN";
        	memberRole = MemberRole.SUBADMIN;
            break;
        case 3:
//            role = "ROLE_BASIC";
        	memberRole = MemberRole.BASIC;
            break;
            default:
                throw new DataNotFoundException("Invalid role option");
        }
    	member.setMemberRole(memberRole);
    	this.memberRepository.save(member);
    }
}
