package com.web.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagForm {
	@NotEmpty(message="공백은 태그가 될 수 없습니다")
	public String tagName;

}
