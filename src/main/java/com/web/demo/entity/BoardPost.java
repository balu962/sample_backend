package com.web.demo.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class BoardPost {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Size(max=50)
	@Column(nullable = false)
	private String subject;
	@Column(nullable = false)
	private String content;
	private LocalDateTime createDate;
	@Column(nullable = false)
	private Long views;
	
	@ManyToOne
	private Member member;
	@ManyToOne
	@JoinColumn(name = "board_id")
	private Board board;
	@OneToMany(mappedBy = "boardPost", cascade = CascadeType.REMOVE)
	private List<BoardComment> boardComments;
	@OneToMany(mappedBy = "boardPost", cascade = CascadeType.REMOVE)
	private List<PostTagMap> PostTagMaps;
}
