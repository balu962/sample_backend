package com.web.demo.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.web.demo.entity.BoardPost;
import com.web.demo.entity.Member;

import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostForm {	
	private Long id;
	private Long listId;
	@Transient
	private Long boardId;
	@NotEmpty(message="제목을 적어주세요")
	@Size(max=40, message="제목은 최대 40자까지 작성 가능합니다")
	private String subject;
	@NotEmpty(message="내용을 적어주세요")
	private String content;	
	private LocalDateTime createDate;
	private String nickname;
	private Long views;
	@Transient
	private List<String> tags;
	private Long memberId;
	@Builder
	public PostForm(Long id, String subject,String content
			, LocalDateTime createDate, Long views, String nickname
			, Long boardId, List<String> tags, Long listId, Long memberId) {
		super();
		this.id = id;
		this.subject = subject;
		this.content = content;
		this.createDate = createDate;
		this.views = views;
		this.nickname = nickname;
		this.boardId = boardId;
		this.tags = tags;
		this.listId = listId;
		this.memberId = memberId;
	}
	
}
