package com.web.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.web.demo.dto.BoardForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.entity.Board;
import com.web.demo.service.BoardService;
import com.web.demo.service.CommonService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;


@RequiredArgsConstructor
@Controller
@RequestMapping("/board")
public class BoardController {
	private final BoardService boardService;
	private final CommonService commonService;
	
	// 게시판 관리
	@GetMapping("/control")
	public String control(Model model
			,@AuthenticationPrincipal MemberUserDto userDetails) {
		List<Board> boards = this.boardService.getAllBoard();
		model.addAttribute("boards", boards);
		this.commonService.commonBoardList(model, userDetails);
		return "board_control";
	}

	// 게시판 생성
	@GetMapping("/create")
	public String create1(BoardForm boardForm, Model model
			,@AuthenticationPrincipal MemberUserDto userDetails) {
		model.addAttribute("method", "post");
		this.commonService.commonBoardList(model, userDetails);
		return "board_form";
	}
	@PostMapping("/create")
	public String create2(@Valid BoardForm boardForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "board_form";
		}
		this.boardService.create(boardForm);
		return "redirect:/board/control";
	}
	// 게시판 수정
	@GetMapping("/{boardId}/modify")
	public String modify1(@PathVariable(value="boardId") Long boardId, Model model
			,BoardForm boardForm
			,@AuthenticationPrincipal MemberUserDto userDetails) {
		boardForm = this.boardService.getOneBoardForm(boardId);
	    if (boardForm == null) {
	        return "/lib/error404";}
		model.addAttribute("method", "put");
		model.addAttribute("boardForm",boardForm);
		this.commonService.commonBoardList(model, userDetails);
		return "board_form";
	}

	@PostMapping("/{boardId}/modify")
	public String modify2(@PathVariable(value="boardId") Long boardId
	,@Valid BoardForm boardForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "board_form";
		}
		this.boardService.modify(boardForm,boardId);
		return "redirect:/board/control";
	}
	// 게시판 삭제
	@PostMapping("/{boardId}/delete")
	public String getMethodName(@PathVariable(value="boardId") Long boardId) {
		this.boardService.delete(boardId);
		return "redirect:/board/control";
	}
	

}
