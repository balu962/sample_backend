package com.web.demo.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.demo.entity.BoardPost;
import com.web.demo.entity.PostTagMap;
import com.web.demo.entity.Tag;
import com.web.demo.repository.PostTagMapRepository;

@Service
public class PostTagMapService {
	@Autowired
	private PostTagMapRepository postTagMapRepository;
	

    public void create(BoardPost post, List<Tag> tagList) {
        for (Tag tag : tagList) {
            PostTagMap postTagMap = new PostTagMap();
            postTagMap.setTag(tag);
            postTagMap.setBoardPost(post);
            postTagMapRepository.save(postTagMap);
        }
    }
    public void modify(BoardPost post, List<Tag> newTags) {
    	// 비교를 위해 기존 태그맵 가져옴
        List<PostTagMap> oldTagMaps = postTagMapRepository.findByBoardPostId(post.getId());
        // 기존 태그 목록 추출
        List<Tag> oldTags = oldTagMaps.stream()
                .map(PostTagMap::getTag)
                .collect(Collectors.toList());
        // 삭제된 태그 제거
        for (PostTagMap tagMap : oldTagMaps) {
        	// 업데이트된 태그 목록에 해당되지 않는 태그 검사
            if (!newTags.contains(tagMap.getTag())) {
            	// 발견시 태그맵에서 삭제
                postTagMapRepository.delete(tagMap);
            }
        }
        // 새로운 태그 추가
        for (Tag tag : newTags) {
        	// 업데이트된 태그 목록에서 기존에 입력한 태그가 아닌지 검사
            if (!oldTags.contains(tag)) {
            	// 기존에 없던 태그면 태그맵에 입력
                PostTagMap postTagMap = new PostTagMap();
                postTagMap.setTag(tag);
                postTagMap.setBoardPost(post);
                postTagMapRepository.save(postTagMap);
            }
        }
    }
}
