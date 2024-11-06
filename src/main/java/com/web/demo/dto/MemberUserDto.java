package com.web.demo.dto;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberUserDto extends User {
	private Long memberId;
	private String email;
	private String nickname;
	
    public MemberUserDto(Long memberId, String username, String password, String nickname
    		,Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.memberId = memberId;
		this.email=username;
		this.nickname=nickname;
	}
}