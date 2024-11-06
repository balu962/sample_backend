package com.web.demo.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.web.demo.dto.BoardForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.entity.Board;
import com.web.demo.entity.BoardPost;
import com.web.demo.repository.BoardPostRepository;
import com.web.demo.repository.BoardRepository;

@Service
public class CommonService {
	@Autowired
	private BoardService boardService;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private BoardPostRepository boardPostRepository;
	@Autowired
	private BoardPostService boardPostService;

	public void commonBoardPostIdModel(Model model, Long boardPostId) {
		BoardForm boardForm = this.boardService
				.getOneBoardForm(this.boardPostRepository.findById(boardPostId).get().getBoard().getId());
		model.addAttribute("boardForm", boardForm);
		model.addAttribute("boardPostId", boardPostId);
	}

	public void commonBoardList(Model model, MemberUserDto userDetails) {
		List<Board> boards = this.boardRepository.findAll(Sort.by("id"));
		model.addAttribute("boards", boards);
		if (userDetails != null) {
			model.addAttribute("nickname", userDetails.getNickname());
		} else {
			model.addAttribute("nickname", "익명의 사용자");
		}
	}

	public void indexBoardList(Model model, MemberUserDto userDetails) {
		List<Board> boards = this.boardRepository.findAll(Sort.by("id"));
		model.addAttribute("boards", boards);
		if (userDetails != null) {
			model.addAttribute("nickname", userDetails.getNickname());
		} else {
			model.addAttribute("nickname", "익명의 사용자");
		}
		if (boards != null && !boards.isEmpty()) {
			Page<PostForm> bbsList1 = this.boardPostService.bbsList(0, boards.get(0).getId());
			model.addAttribute("bbsList1", bbsList1);

			if (boards.size() > 1) {
				Page<PostForm> bbsList2 = this.boardPostService.bbsList(0, boards.get(1).getId());
				model.addAttribute("bbsList2", bbsList2);
			}
		}
	}

	public void commonBoardView(Model model, Long boardId) {
		model.addAttribute("boardForm", this.boardService.getOneBoardForm(boardId));
	}
}
