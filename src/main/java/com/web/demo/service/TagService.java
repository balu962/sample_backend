package com.web.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.web.demo.dto.TagForm;
import com.web.demo.entity.PostTagMap;
import com.web.demo.entity.Tag;
import com.web.demo.repository.TagRepository;

import jakarta.validation.Valid;

@Service
public class TagService {
	@Autowired
	private TagRepository tagRepository;
	public void create(String tagName) {
		this.tagRepository.save(new Tag(tagName));
	}
	public void check(String tagName) {
		this.tagRepository.findByTagName(tagName);
	}
	public void checkCreate(@Valid TagForm tagForm, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			System.out.println("태그 공란");
		}else {
		try {
			this.create(tagForm.getTagName());
			System.out.println("태그 생성");
		} catch (DataIntegrityViolationException e) {
			bindingResult.reject("signupError", "이미 등록된 태그");
			System.out.println("태그 중복");
		}
	}
	}
	public List<Tag> tagsProcess(List<String> tags) {
		// 받아온 tags에서 걸러내기
	    List<String> validTags = tags.stream()
	            .filter(tag -> !tag.isBlank()) // null과 공백 제거
	            .distinct() // 중복 제거
	            .collect(Collectors.toList());
	    
	    // post랑 tag 매핑을 위한 태그 리스트 
	    List<Tag> tagList = new ArrayList<>();
	    // 기존 tag 테이블 확인해서 없으면 태그 생성
	    for (String tagName : validTags) {
            Tag tag = tagRepository.findByTagName(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
                tagRepository.save(tag);
            }
            tagList.add(tag);
	        }
	    return tagList;
	}
}
