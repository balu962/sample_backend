package com.web.demo.dto;

import java.time.LocalDateTime;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentForm {
    private Long id;
	@NotEmpty
	@Size(max = 200, message = "댓글은 200자 이하로 작성해주세요")
	private String content;
	private String nickname;
	private LocalDateTime createDate;
	
}
