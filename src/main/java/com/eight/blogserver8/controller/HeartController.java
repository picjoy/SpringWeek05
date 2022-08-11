package com.eight.blogserver8.controller;

import com.eight.blogserver8.controller.response.ResponseDto;
import com.eight.blogserver8.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
public class HeartController {

    private final HeartService heartService;

    //POST로 보내면 좋아요, DELETE로 보내면 좋아요 취소

    // 게시물 좋아요
    @PostMapping("/api/auth/heart/post/{postId}")
    public ResponseDto<?> heartPost(@PathVariable Long postId, HttpServletRequest request){
        return heartService.heartPost(postId, request);
    }

    // 댓글 좋아요
    @PostMapping("/api/auth/heart/comment/{commentId}")
    public ResponseDto<?> heartComment(@PathVariable Long commentId, HttpServletRequest request){
        return heartService.heartComment(commentId, request);
    }

    // 대댓글 좋아요
    @PostMapping("/api/auth/heart/subComment/{subCommentId}")
    public ResponseDto<?> heartSubComment(@PathVariable Long subCommentId, HttpServletRequest request){
        return heartService.heartSubComment(subCommentId, request);
    }

    // 게시물 좋아요 취소
    @DeleteMapping ("/api/auth/unHeart/post/{postId}")
    public ResponseDto<?> unHeartPost(@PathVariable Long postId, HttpServletRequest request){
        return heartService.unHeartPost(postId, request);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/api/auth/unHeart/comment/{commentId}")
    public ResponseDto<?> unHeartComment(@PathVariable Long commentId, HttpServletRequest request){
        return heartService.unHeartComment(commentId, request);
    }

    // 대댓글 좋아요 취소
    @DeleteMapping("/api/auth/unHeart/subComment/{subCommentId}")
    public ResponseDto<?> unHeartSubComment(@PathVariable Long subCommentId, HttpServletRequest request){
        return heartService.unHeartSubComment(subCommentId, request);
    }




}
