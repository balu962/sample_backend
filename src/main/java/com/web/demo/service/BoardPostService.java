package com.web.demo.service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.web.demo.dto.MemberUserDto;
import com.web.demo.dto.PostForm;
import com.web.demo.dto.PostForm.PostFormBuilder;
import com.web.demo.entity.Board;
import com.web.demo.entity.BoardPost;
import com.web.demo.entity.Member;
import com.web.demo.entity.PostTagMap;
import com.web.demo.entity.Tag;
import com.web.demo.exception.DataNotFoundException;
import com.web.demo.repository.BoardPostRepository;
import com.web.demo.repository.BoardRepository;
import com.web.demo.repository.MemberRepository;
import com.web.demo.repository.PostTagMapRepository;
import com.web.demo.repository.TagRepository;

@Service
public class BoardPostService {
	@Autowired
	private BoardPostRepository boardPostRepository;
	@Autowired
	private BoardRepository boardRepository;
	@Autowired
	private TagService tagService;
	@Autowired
	private PostTagMapService postTagMapService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private PostTagMapRepository postTagMapRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	private MemberService memberService;
	// DI
    @Autowired
    public BoardPostService(PostTagMapService postTagMapService, TagService tagService, 
                            BoardPostRepository boardPostRepository, BoardRepository boardRepository) {
        this.postTagMapService = postTagMapService;
        this.tagService = tagService;
        this.boardPostRepository = boardPostRepository;
        this.boardRepository = boardRepository;
    }
    // 페이지 사이즈 지정
	private final int PAGE_SIZE = 10;
	
	// 존재하는 BoardPost인지 id로 확인
	public BoardPost getOneBoardPost(Long boardPostId) {
		return this.boardPostRepository.findById(boardPostId)
				// 없으면 커스텀 오류로 날림
				.orElseThrow(() -> new DataNotFoundException("post not found" + boardPostId));
	}
		// 존재하지 않는 post 처리

	// 게시물 생성
	public void create(PostForm postForm, Long boardId , MemberUserDto userDetails) {
		Board board= this.getOneBoard(boardId);
		BoardPost post = new BoardPost();
		post.setSubject(postForm.getSubject());
		post.setContent(postForm.getContent());
		post.setCreateDate(LocalDateTime.now());
		post.setViews(0L);
		post.setMember(memberRepository.findByEmail(userDetails.getEmail()));
		post.setBoard(board);
		List<Tag> tagList = this.tagService.tagsProcess(postForm.getTags());
		this.boardPostRepository.save(post);
		this.postTagMapService.create(post, tagList);
	}
	// boardId로 존재하는 board인지 확인
	public Board getOneBoard(Long boardId)  {
		return this.boardRepository.findById(boardId)
				.orElseThrow(() -> new DataNotFoundException("board not found" + boardId));
	}
	
	// 모든 게시물 가져오기
	public Page<BoardPost> getList(int page) {
		Pageable pageable = PageRequest.of(page, PAGE_SIZE);
		return boardPostRepository.findAll(pageable);
	}
	// 게시판 별 페이징
	public Page<PostForm> bbsList(int page, Long boardId) {
	    Pageable pageable = PageRequest.of(page, PAGE_SIZE);
	    Page<BoardPost> boardPosts = this.boardPostRepository.findByBoardIdOrderByIdDesc(boardId, pageable);
	    long totalElements = boardPosts.getTotalElements(); // 해당 게시판 총 게시물 수
	    // 해당 게시판 게시물에 붙이는 임시 번호 오래된 글부터 1번으로 시작해야 돼서 총 게시물, 페이지 개수 등의 영향을 받게 설정
	    AtomicLong postId = new AtomicLong(totalElements - (page * PAGE_SIZE));
	    // 해당 게시판의 번호 붙이기 등
	    List<PostForm> bbsPostLists = boardPosts.stream()
	            .map(boardPost -> {
	                PostForm postForm = this.postFormbuild(boardPost.getId());
	                // atomiclong 명령어로 감소
	                postForm.setListId(postId.getAndDecrement());
	                return postForm;
	            })
	            .collect(Collectors.toList());
	    return new PageImpl<>(bbsPostLists, pageable, totalElements);
	}
	// 게시물 각종 정보 폼에 담는 메소드
	// 수정, 조회시 사용
	public PostForm postFormbuild(Long boardpostId) {
		BoardPost post = this.getOneBoardPost(boardpostId);	
		List<String> tags = this.tagsByPost(post);		
		return PostForm.builder()
				.id(boardpostId)
				.subject(post.getSubject())
				.content(post.getContent())
				.createDate(post.getCreateDate())
				.views(post.getViews())
				.tags(tags)
				.nickname(post.getMember().getNickname())
				.memberId(post.getMember().getId())
				.boardId(post.getBoard().getId())
				.build();
	}
	
	// 게시물 조회시 사용
	public PostForm viewPost(Long boardpostId)  {
			BoardPost post = this.getOneBoardPost(boardpostId);
			post.setViews(post.getViews()+1);
			this.boardPostRepository.save(post);
			return this.postFormbuild(boardpostId);
	}
	// 게시물 태그 불러오기용 공통 사용 메소드
	public List<String> tagsByPost(BoardPost post){
        return post.getPostTagMaps().stream()
                .map(postTagMap -> postTagMap.getTag().getTagName())
                .collect(Collectors.toList());
	}
	// 게시물 수정
	public void modify(Long boardPostId, PostForm postForm) {
		BoardPost post = this.getOneBoardPost(boardPostId);
		post.setSubject(postForm.getSubject());
		post.setContent(postForm.getContent());
		// 태그 갱신 처리
		List<Tag> newTagList = tagService.tagsProcess(postForm.getTags());
        postTagMapService.modify(post, newTagList);
		this.boardPostRepository.save(post);
	}
	// 게시물 삭제
	public void delete(Long boardpostId) {
		Optional<BoardPost> oPost = this.boardPostRepository.findById(boardpostId);
		BoardPost post = oPost.get();
		this.boardPostRepository.delete(post);
	}
	// 유저 권한 조회
	public boolean memberPostAccess(MemberUserDto userDetails, Long boarPostId) {
		Member origMember = this.boardPostRepository.findById(boarPostId).get().getMember();
		return this.memberService.checkAccess(userDetails, origMember);
	}

}
