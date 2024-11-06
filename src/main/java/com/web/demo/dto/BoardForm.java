package com.web.demo.dto;


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
public class BoardForm {	
	@Transient
	private Long boardId;
	@NotEmpty(message="게시판 이름을 적어주세요")
	@Size(max=40, message="게시판은 최대 20자까지 작성 가능합니다")
	public String boardName;
	@NotEmpty(message="게시판 설명을 적어주세요")
	@Size(max=100, message="게시판 설명은 최대 100자까지 작성 가능합니다")
	public String description;	
	
	@Builder
	public BoardForm(Long boardId, String boardName,String description) {
		super();
		this.boardId = boardId;
		this.boardName = boardName;
		this.description = description;
	}
}
