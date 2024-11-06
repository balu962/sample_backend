package com.web.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.PostTagMap;

public interface PostTagMapRepository extends JpaRepository<PostTagMap, Long> {
	List<PostTagMap> findByTagId(Long tagId);
	List<PostTagMap> findByBoardPostId(Long boardPostId);
}
