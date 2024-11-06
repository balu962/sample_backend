package com.web.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.web.demo.config.MemberRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(nullable = false)
	private String password;
	@Size(max=30)
	@Column(unique = true, nullable = false)
	private String nickname;
	private LocalDateTime createDate;
//	private String memberRole;
    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;
	
	
	@OneToMany(mappedBy = "member")
	private List<BoardPost> boardPosts;
	@OneToMany(mappedBy = "member")
	private List<BoardComment> Comments;
}

