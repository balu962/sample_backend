package com.web.demo.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.entity.Board;
import com.web.demo.service.BoardPostService;
import com.web.demo.service.BoardService;
import com.web.demo.service.CommonService;
import com.web.demo.service.MemberService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
public class MainController {
	private final MemberService memberService;
	private final BoardService boardService;
	private final CommonService commonService;
	private final BoardPostService boardPostService;
	

	@RequestMapping("/")
	public String main(Model model
			,@AuthenticationPrincipal MemberUserDto userDetails) {
		this.commonService.indexBoardList(model, userDetails);
		return "index";
	}
	@GetMapping("/test")
	public String getMethodName() {
		return "/main/test";
	}
	

	
}
