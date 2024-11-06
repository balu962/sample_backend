package com.web.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.BoardComment;

public interface BoardCommentRepository extends JpaRepository<BoardComment, Long> {
	BoardComment findByBoardPostId(Long boardPostId);
	List<BoardComment> findByBoardPostIdOrderByIdAsc(Long boardPostId);
	
}
