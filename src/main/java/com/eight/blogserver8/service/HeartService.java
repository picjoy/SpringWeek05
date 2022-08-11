package com.eight.blogserver8.service;

import com.eight.blogserver8.controller.response.CommentResponseDto;
import com.eight.blogserver8.controller.response.HeartResponseDto;
import com.eight.blogserver8.controller.response.ResponseDto;
import com.eight.blogserver8.controller.response.SubCommentResponseDto;
import com.eight.blogserver8.domain.*;
import com.eight.blogserver8.jwt.TokenProvider;
import com.eight.blogserver8.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HeartService {

    private final PostService postService;
    private final CommentService commentService;
    private final SubCommentService subCommentService;

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final HeartPostRepository heartPostRepository;
    private final HeartCommentRepository heartCommentRepository;
    private final HeartSubCommentRepository heartSubCommentRepository;
    private final TokenProvider tokenProvider;


    // 게시글 좋아요
    public ResponseDto<?> heartPost(Long postId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        // 게시글 존재 확인
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // 중복사용자 같은 게시글 좋아요 금지.
        Long checkHeart = heartPostRepository.countByMemberIdAndPostId(member.getId(),post.getId());
        if(checkHeart > 0){
            return ResponseDto.fail("NOT_FOUND", "좋아요 중복은 금지입니다.");
        }

        // 좋아요 저장
        HeartPost heartPost = HeartPost.builder()
                .member(member)
                .post(post)
                .build();
        heartPostRepository.save(heartPost);
        Long heart = heartPostRepository.countAllByPostId(post.getId());
        post.updateHeart(heart);
        postRepository.save(post);
        // 게시물 업데이트!!



        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {

            List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
            List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();

            for (SubComment subComment : subCommentList) {
                subCommentResponseDtoList.add(
                        SubCommentResponseDto.builder()
                                .id(subComment.getId())
                                .nickname(subComment.getMember().getNickname())
                                .content(subComment.getContent())
                                .heart(subComment.getHeart())
                                .createdAt(subComment.getCreatedAt())
                                .modifiedAt(subComment.getModifiedAt())
                                .build()
                );
            }


            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .subCommentResponseDtoList(subCommentResponseDtoList) // 대댓글 넣기
                            .heart(comment.getHeart()) // 좋아요
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        // 게시글 Dto
        HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getNickname())
                .commentResponseDtoList(commentResponseDtoList) //댓글 Dto 리스트
                .heart(post.getHeart()) // 좋아요
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();


        return ResponseDto.success(heartResponseDto);

    }

    // 댓글 좋아요
    public ResponseDto<?> heartComment(Long commentId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 댓글 존재 확인
        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        // 좋아요 저장
        HeartComment heartComment = HeartComment.builder()
                .member(member) //comment.getMember() 코멘트 작성한 사람이 좋아요이름으로 나옴.
                .comment(comment)
                .build();
        heartCommentRepository.save(heartComment);

        // 댓글 좋아요 업데이트
        Long heart = heartCommentRepository.countAllByCommentId(comment.getId());
        comment.updateHeart(heart);
        commentRepository.save(comment);

        List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
        List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();

        for (SubComment subComment : subCommentList) {
            subCommentResponseDtoList.add(
                    SubCommentResponseDto.builder()
                            .id(subComment.getId())
                            .nickname(subComment.getMember().getNickname())
                            .content(subComment.getContent())
                            .heart(subComment.getHeart())
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .author(comment.getMember().getNickname())
                .content(comment.getContent())
                .subCommentResponseDtoList(subCommentResponseDtoList)
                .heart(comment.getHeart())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        return ResponseDto.success(commentResponseDto);


    }


    // 대댓글 좋아요
    public ResponseDto<?> heartSubComment(Long subCommentId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 대댓글 존재 확인
        SubComment subComment = subCommentService.isPresentSubComment(subCommentId);
        if (null == subComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }


        // 좋아요 저장
        HeartSubComment heartSubComment = HeartSubComment.builder()
                .member(member) ///subComment.getMember() 코멘트 작성한 사람이 좋아요이름으로 나옴.
                .subComment(subComment)
                .build();
        heartSubCommentRepository.save(heartSubComment);


        // 대댓글 좋아요 업데이트
        Long heart = heartSubCommentRepository.countAllBySubCommentId(subComment.getId());
        subComment.updateHeart(heart);
        subCommentRepository.save(subComment);

        SubCommentResponseDto subCommentResponseDto = SubCommentResponseDto.builder()
                .id(subComment.getId())
                .nickname(subComment.getMember().getNickname())
                .content(subComment.getContent())
                .heart(subComment.getHeart())
                .heart(subComment.getHeart())
                .createdAt(subComment.getCreatedAt())
                .modifiedAt(subComment.getModifiedAt())
                .build();

        return ResponseDto.success(subCommentResponseDto);


    }


    // 게시물 좋아요 취소
    public ResponseDto<?> unHeartPost(Long postId, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        // 게시글 존재 확인
        Post post = postService.isPresentPost(postId);
        if (null == post) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 게시글 id 입니다.");
        }

        // 게시글 좋아요 수 삭제
        Long heartTotal = heartPostRepository.countAllByPostId(post.getId());
        if (heartTotal != 0) {
            heartPostRepository.deleteByPostId(post.getId());
        }

        // 게시물 좋아요 업데이트
        Long heart = heartPostRepository.countAllByPostId(post.getId());
        post.updateHeart(heart);
        postRepository.save(post);

        // 댓글 리스트의 Dto화
        List<Comment> commentList = commentRepository.findAllByPost(post);
        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();

        for (Comment comment : commentList) {


            List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
            List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();

            for (SubComment subComment : subCommentList) {
                subCommentResponseDtoList.add(
                        SubCommentResponseDto.builder()
                                .id(subComment.getId())
                                .nickname(subComment.getMember().getNickname())
                                .content(subComment.getContent())
                                .heart(subComment.getHeart())
                                .createdAt(subComment.getCreatedAt())
                                .modifiedAt(subComment.getModifiedAt())
                                .build()
                );
            }


            commentResponseDtoList.add(
                    CommentResponseDto.builder()
                            .id(comment.getId())
                            .author(comment.getMember().getNickname())
                            .content(comment.getContent())
                            .subCommentResponseDtoList(subCommentResponseDtoList) // 대댓글 넣기
                            .heart(comment.getHeart()) // 좋아요
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        // 게시글 Dto
        HeartResponseDto heartResponseDto = HeartResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .author(post.getMember().getNickname())
                .commentResponseDtoList(commentResponseDtoList) //댓글 Dto 리스트
                .heart(post.getHeart()) // 좋아요
                .createdAt(post.getCreatedAt())
                .modifiedAt(post.getModifiedAt())
                .build();


        return ResponseDto.success(heartResponseDto);

    }

    // 댓글 좋아요 취소
    public ResponseDto<?> unHeartComment(Long commentId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 댓글 존재 확인
        Comment comment = commentService.isPresentComment(commentId);
        if (null == comment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        Long heartTotal = heartCommentRepository.countAllByCommentId(comment.getId());
        if (heartTotal != 0) {
            heartCommentRepository.deleteByCommentId(heartTotal);
        }

        // 댓글 좋아요 업데이트
        Long heart = heartCommentRepository.countAllByCommentId(comment.getId());
        comment.updateHeart(heart);
        commentRepository.save(comment);


        List<SubComment> subCommentList = subCommentRepository.findAllByComment(comment);
        List<SubCommentResponseDto> subCommentResponseDtoList = new ArrayList<>();

        for (SubComment subComment : subCommentList) {
            subCommentResponseDtoList.add(
                    SubCommentResponseDto.builder()
                            .id(subComment.getId())
                            .nickname(subComment.getMember().getNickname())
                            .content(subComment.getContent())
                            .heart(subComment.getHeart())
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }

        CommentResponseDto commentResponseDto = CommentResponseDto.builder()
                .id(comment.getId())
                .author(comment.getMember().getNickname())
                .content(comment.getContent())
                .subCommentResponseDtoList(subCommentResponseDtoList)
                .heart(comment.getHeart())
                .createdAt(comment.getCreatedAt())
                .modifiedAt(comment.getModifiedAt())
                .build();

        return ResponseDto.success(commentResponseDto);


    }

    // 대댓글 좋아요 취소
    public ResponseDto<?> unHeartSubComment(Long subCommentId, HttpServletRequest request) {

        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        // 대댓글 존재 확인
        SubComment subComment = subCommentService.isPresentSubComment(subCommentId);
        if (null == subComment) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
        }

        Long heartTotal = heartSubCommentRepository.countAllBySubCommentId(subComment.getId());
        if (heartTotal != 0) {
            heartSubCommentRepository.deleteBySubCommentId(heartTotal);
        }
            // 대댓글 좋아요 업데이트
            Long heart = heartSubCommentRepository.countAllBySubCommentId(subComment.getId());
            subComment.updateHeart(heart);
        subCommentRepository.save(subComment);


            SubCommentResponseDto subCommentResponseDto = SubCommentResponseDto.builder()
                    .id(subComment.getId())
                    .nickname(subComment.getMember().getNickname())
                    .content(subComment.getContent())
                    .heart(subComment.getHeart())
                    .heart(subComment.getHeart())
                    .createdAt(subComment.getCreatedAt())
                    .modifiedAt(subComment.getModifiedAt())
                    .build();

            return ResponseDto.success(subCommentResponseDto);


        }


        @Transactional
        public Member validateMember (HttpServletRequest request){
            if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
                return null;
            }
            return tokenProvider.getMemberFromAuthentication();
        }


    }

