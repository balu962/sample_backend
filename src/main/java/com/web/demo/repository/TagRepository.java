package com.web.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.web.demo.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	Tag findByTagName(String tagname);
	boolean existsByTagName(String tagName);

}
