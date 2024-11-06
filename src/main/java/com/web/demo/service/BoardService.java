package com.web.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.demo.dto.BoardForm;
import com.web.demo.entity.Board;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.BoardRepository;


@Service
public class BoardService {
	@Autowired
	private BoardRepository boardRepository;
	
	public List<Board> getAllBoard() {
		return this.boardRepository.findAll(Sort.by(Sort.Order.asc("id")));
	}
	public List<Board> getBoardList() {
		return this.boardRepository.findAll();
	}
	public void create(BoardForm boardForm) {
		Board board = new Board();
		board.setBoardName(boardForm.getBoardName());
		board.setDescription(boardForm.getDescription());
		this.boardRepository.save(board);
	}
	public String findBoardName(Long boardId) {
		Optional<Board> oBoard = this.boardRepository.findById(boardId);
		Board board = oBoard.get();
		return board.getBoardName();
	}
	public BoardForm getOneBoardForm(Long boardId) {
		Board board = this.getOneBoard(boardId);
		return BoardForm.builder().boardId(board.getId()).boardName(board.getBoardName()).description(board.getDescription()).build();
		}
	public Board getOneBoard(Long boardId) {
		Optional<Board> oBoard = this.boardRepository.findById(boardId);
		if (oBoard.isPresent()) {
			return oBoard.get();
		}
		throw new DataNotFoundException("board not found");
	}
	public void modify(BoardForm boardForm, Long boardId) {
		Board board = this.getOneBoard(boardId);
		board.setBoardName(boardForm.getBoardName());
		board.setDescription(boardForm.getDescription());
		this.boardRepository.save(board);
	}
	public void delete(Long boardId) {
		Optional<Board> oBoard = this.boardRepository.findById(boardId);
		Board board = oBoard.get();
		this.boardRepository.delete(board);
	}

}
