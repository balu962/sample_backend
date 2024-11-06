package com.web.demo.dto;

import java.time.LocalDateTime;

import com.web.demo.config.MemberRole;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberForm {
	private Long id;
	@NotEmpty(message="닉네임을 입력해주세요")
	@Size(max = 30, message = "닉네임은 30자 이하로 해주세요")
	private String nickname;
	@NotEmpty(message="비밀번호를 입력해주세요")
	private String password;
	@NotEmpty(message="비밀번호 확인을 위해 꼭 입력해주세요")
	private String password1;
	@NotEmpty(message="이메일을 입력해주세요")
	@Email
	private String email;
	private LocalDateTime createDate;
	private String memberRole;
}
