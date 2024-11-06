package com.web.demo.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.web.demo.dto.CommentForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.entity.BoardComment;
import com.web.demo.service.BoardCommentService;
import com.web.demo.service.CommonService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
public class CommentController {
	private final BoardCommentService commentService;
	private final CommonService commonService;
	
	@PostMapping("/bbs/{boardId}/post/{boardPostId}/comment/create")
	public String create(@PathVariable(value="boardId") Long boardId
			,@PathVariable(value="boardPostId") Long boardPostId
			, Model model
			,@AuthenticationPrincipal MemberUserDto userDetails
			,@Valid CommentForm commentForm
			, BindingResult bindingResult) {
		if( bindingResult.hasErrors() ) {
			this.commonService.commonBoardPostIdModel(model, boardPostId);
			return "redirect:/bbs/{boardId}/post/{boardPostId}";
		}
		this.commentService.create(commentForm, boardPostId, userDetails);
		return "redirect:/bbs/{boardId}/post/{boardPostId}";
	}
	// 댓글 삭제
	@PostMapping("/bbs/{boardId}/post/{boardPostId}/comment/delete/{boardCommentId}")
	public String delete(@PathVariable(value="boardId") Long boardId
			,@PathVariable(value="boardPostId") Long boardPostId
			,@PathVariable(value="boardCommentId") Long boardCommentId
			, Model model
			,@AuthenticationPrincipal MemberUserDto userDetails
			,@Valid CommentForm commentForm
			, BindingResult bindingResult) {
		this.commentService.delete(boardCommentId);
		return "redirect:/bbs/{boardId}/post/{boardPostId}";
	}
	// 스크립트 잘 안 돼서 댓글 수정 페이지 따로 추가
	@GetMapping("/bbs/{boardId}/post/{boardPostId}/comment/modify/{boardCommentId}")
	public String modify1(@PathVariable(value="boardId") Long boardId
			,@PathVariable(value="boardPostId") Long boardPostId
			,@PathVariable(value="boardCommentId") Long boardCommentId
			,@AuthenticationPrincipal MemberUserDto userDetails
			,CommentForm commentForm
			, Model model){
		model.addAttribute("commentForm", commentForm = commentService.getOneCommentForm(boardCommentId));
		model.addAttribute("boardCommentId", boardCommentId);
		this.commonService.commonBoardPostIdModel(model, boardPostId);
		this.commonService.commonBoardList(model, userDetails);
		return "comment_form";
	}
	
	// 댓글 수정
	@PostMapping("/bbs/{boardId}/post/{boardPostId}/comment/modify/{boardCommentId}")
	public String modify(@PathVariable(value="boardId") Long boardId
			,@PathVariable(value="boardPostId") Long boardPostId
			,@PathVariable(value="boardCommentId") Long boardCommentId
			, RedirectAttributes redirectAttributes
			,@AuthenticationPrincipal MemberUserDto userDetails
			,@Valid CommentForm commentForm
			, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			redirectAttributes.addFlashAttribute("commentError", "댓글 수정에 실패했습니다.");
			return "redirect:/bbs/{boardId}/post/{boardPostId}";
		}
		this.commentService.modify(commentForm, boardCommentId);
		return "redirect:/bbs/{boardId}/post/{boardPostId}";
	}

}
