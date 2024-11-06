package com.web.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "올바르지 않은 주소입니다")
public class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String msg) {
		super(msg);
	}
}
