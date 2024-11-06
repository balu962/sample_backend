package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.web.demo.dto.CommentForm;
import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.entity.BoardComment;
import com.web.demo.entity.BoardPost;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.BoardPostRepository;
import com.web.demo.repository.BoardCommentRepository;
import com.web.demo.repository.MemberRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
public class BoardCommentService {
	@Autowired
	private BoardCommentRepository boardCommentRepository;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private BoardPostRepository boardPostRepository;
	// 생성
	public void create(CommentForm commentForm, Long boardPostId, MemberUserDto userDetails) {
		commentForm.getContent();
		BoardComment boardComment=new BoardComment();
		boardComment.setContent(commentForm.getContent());
		boardComment.setCreateDate(LocalDateTime.now());
		boardComment.setMember(memberRepository.findByEmail(userDetails.getEmail()));
		Optional<BoardPost> oPost = boardPostRepository.findById(boardPostId);
		boardComment.setBoardPost(oPost.get());
		boardCommentRepository.save(boardComment);
	}
	// 게시물 하나에 대한 댓글 목록
	public List<CommentForm> CommentListByBoardPostId(Long boardPostId) {
        List<BoardComment> comments = boardCommentRepository.findByBoardPostIdOrderByIdAsc(boardPostId);
        return comments.stream()
                       .map(this::commentFormByComment)
                       .collect(Collectors.toList());
    }
	// 코멘트를 코멘트 폼으로 만듦
    private CommentForm commentFormByComment(BoardComment comment) {
    	CommentForm commentForm = new CommentForm();
    	commentForm.setId(comment.getId());
    	commentForm.setContent(comment.getContent());
    	commentForm.setNickname(comment.getMember().getNickname());
    	commentForm.setCreateDate(comment.getCreateDate());
        return commentForm;
    }
    // 댓글 하나 찾아주는 거
    public BoardComment getOneBoardComment(Long boardCommentId) {
    	return this.boardCommentRepository.findById(boardCommentId)
    			.orElseThrow(() -> new DataNotFoundException("comment not found" + boardCommentId));
    }
    public CommentForm getOneCommentForm(Long boardCommentId) {
    	return this.commentFormByComment(this.getOneBoardComment(boardCommentId));
    }
    // 댓글 삭제
	public void delete(Long boardCommentId) {
//		BoardComment comment = this.getOneBoardComment(boardCommentId);
//		this.boardCommentRepository.delete(comment);
		this.boardCommentRepository.deleteById(boardCommentId);
	}
	// 댓글 수정
	public void modify(CommentForm commentForm, Long boardCommentId) {
		BoardComment boardComment = this.getOneBoardComment(boardCommentId); 
		boardComment.setContent(commentForm.getContent());
		this.boardCommentRepository.save(boardComment);
	}
}
