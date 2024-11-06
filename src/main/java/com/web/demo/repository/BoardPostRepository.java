package com.web.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.BoardPost;
public interface BoardPostRepository extends JpaRepository<BoardPost, Long> {
	BoardPost findBySubject(String subject);
	BoardPost findByContent (String content);
	BoardPost findByBoardId (Long boardId);
	Page<BoardPost> findAll(Pageable pageable); 
	Page<BoardPost> findAll(Specification<BoardPost> sf, Pageable pageable);
	Page<BoardPost> findByBoardId(Long boardId, Pageable pageable);
	Page<BoardPost> findByBoardId(Long boardId, Specification<BoardPost> sf, Pageable pageable);
	// Desc 역순으로 게시판 별 게시물
	Page<BoardPost> findByBoardIdOrderByIdDesc(Long boardId, Specification<BoardPost> sf, Pageable pageable);
	Page<BoardPost> findByBoardIdOrderByIdDesc(Long boardId, Pageable pageable);
	// Asc 순차대로 게시판 별 게시물
	Page<BoardPost> findByBoardIdOrderByIdAsc(Long boardId, Specification<BoardPost> sf, Pageable pageable);
	Page<BoardPost> findByBoardIdOrderByIdAsc(Long boardId, Pageable pageable);

}
