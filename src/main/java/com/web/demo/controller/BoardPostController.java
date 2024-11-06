package com.web.demo.controller;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.RequiredArgsConstructor;

import com.web.demo.dto.CommentForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.dto.TagForm;
import com.web.demo.entity.Board;
import com.web.demo.service.BoardCommentService;
import com.web.demo.service.BoardPostService;
import com.web.demo.service.BoardService;
import com.web.demo.service.CommonService;
import com.web.demo.service.TagService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RequiredArgsConstructor
@Controller
@RequestMapping("/bbs")
public class BoardPostController {
	private final CommonService commonService;
	private final BoardPostService postService;
	private final TagService tagService;
	private final BoardService boardService;
	private final BoardCommentService boardCommentService;
	

// 게시물 목록 파트
@GetMapping("/{boardId}/list")
public String postList(@RequestParam(value="page", defaultValue = "0") Integer page
		,Model model, @PathVariable(value="boardId") Long boardId
		,@AuthenticationPrincipal MemberUserDto userDetails) {
	model.addAttribute("boardForm",this.boardService.getOneBoardForm(boardId));
	Page<PostForm> paging = this.postService.bbsList(page, boardId);
	model.addAttribute("paging", paging);
	model.addAttribute("boardId", boardId);
	this.commonService.commonBoardList(model, userDetails);
	return "bbs_post_list";
	
}

// 게시물 생성 파트
@GetMapping("/{boardId}/create")
public String postCreate1(PostForm postForm, TagForm tagForm, Model model
		, @PathVariable(value="boardId") Long boardId
		,@AuthenticationPrincipal MemberUserDto userDetails) {
	model.addAttribute("method", "post");
	List<Board> boards = this.boardService.getAllBoard();
	model.addAttribute("boards", boards);
	this.commonService.commonBoardList(model, userDetails);
	return "post_form";
}
@PostMapping("/{boardId}/create")
public String postCreate2(@Valid PostForm postForm, BindingResult bindingResult 
		, @PathVariable(value="boardId") Long boardId
		,@AuthenticationPrincipal MemberUserDto userDetails) {
	if (bindingResult.hasErrors()) {
		return "post_form";
	}
	this.postService.create(postForm, boardId, userDetails);
	return "redirect:/bbs/{boardId}/list";
}
// 게시물 상세 조회
@GetMapping("/{boardId}/post/{boardPostId}")
public String postDetail(@PathVariable(value="boardId") Long boardId,
		@PathVariable(value="boardPostId") Long boardPostId, Model model
		,@AuthenticationPrincipal MemberUserDto userDetails) {
	PostForm postForm = this.postService.viewPost(boardPostId);
    if (postForm == null) {
        return "/lib/error404";}
    List<CommentForm> commentPaging= this.boardCommentService.CommentListByBoardPostId(boardPostId);
    model.addAttribute("commentPaging",commentPaging);
	model.addAttribute("postForm", postForm);
	this.commonService.commonBoardPostIdModel(model, boardPostId);
	model.addAttribute("boardName",this.boardService.findBoardName(boardId));
	this.commonService.commonBoardList(model, userDetails);
	return "post_detail";
}

// 게시물 수정 파트
// 이후 태그 목록 불러와서 수정하는 기능 추가 예정
@GetMapping("/{boardId}/post/{boardPostId}/modify")
public String postModify1(@PathVariable(value="boardId") Long boardId
		,@PathVariable(value="boardPostId") Long boardPostId
			,Model model, PostForm postform
			,@AuthenticationPrincipal MemberUserDto userDetails) {
	if (! this.postService.memberPostAccess(userDetails,boardPostId)) {
		model.addAttribute("accessErrorMessage", "작성자 또는 관리자만 수정, 삭제를 할 수 있습니다");
	return "redirect:/bbs/{boardId}/post/{boardPostId}";
}
	PostForm postForm = this.postService.viewPost(boardPostId);
    if (postForm == null) {
        return "/lib/error404";}
	model.addAttribute("postForm",postForm);
	model.addAttribute("method", "post");
	model.addAttribute("boardId", boardId);
	model.addAttribute("boardPostId",boardPostId);
    return "post_form";
}

@PostMapping("/{boardId}/post/{boardPostId}/modify")
public String postModify2(@Valid PostForm postForm, BindingResult bindingResult 
		, @PathVariable(value="boardId") Long boardId
		,@PathVariable(value="boardPostId") Long boardPostId
		,Model model) {
	if (bindingResult.hasErrors()) {
		return "post_form";
	}
	System.out.println(postForm.getSubject());
			System.out.println(postForm.getContent());
					System.out.println(postForm.getTags());
	this.postService.modify(boardPostId, postForm);
	System.out.println("redirect:/bbs/{boardId}/list/{boardPostId}");
	return "redirect:/bbs/{boardId}/post/{boardPostId}";
}

// 게시물 삭제
// 현재 태그 연동 삭제 미지원, 엔티티 수정 필요
@PostMapping("/{boardId}/post/{boardPostId}/delete")
public String postDelete(@PathVariable(value="boardId") Long boardId,
		@PathVariable(value="boardPostId") Long boardPostId, Model model
		,@AuthenticationPrincipal MemberUserDto userDetails) {
	if (! this.postService.memberPostAccess(userDetails,boardPostId)) {
		model.addAttribute("accessErrorMessage", "작성자 또는 관리자만 수정, 삭제를 할 수 있습니다");
		return "redirect:/bbs/{boardId}/post/{boardPostId}";
	}
	this.postService.delete(boardPostId);
	model.addAttribute("boardId", boardId);
	model.addAttribute("boardName",this.boardService.findBoardName(boardId));
	List<Board> boards = this.boardService.getAllBoard();
	model.addAttribute("boards", boards);
	return "redirect:/bbs/{boardId}/list";
}

	

}
