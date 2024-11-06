package com.web.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	Board findByBoardName (String boardName);
}
