package com.eight.blogserver8.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MypageResponseDto {

    private List postList;
    private List commentList;
    private List subCommentList;
    private List heartPostList;
    private List heartCommentList;
    private List heartSubCommentList;

    public void update(
            List<MypagePostResponseDto> postListDto,
            List<MypageCommentResponseDto> commentListDto,
            List<MypageSubCommentResponseDto> subcommentListDto,
            List<MypagePostResponseDto> heartPostList,
            List<MypageCommentResponseDto> heartCommentList,
            List<MypageSubCommentResponseDto> heartSubCommentList
            ){
        this.postList = postListDto;
        this.commentList = commentListDto;
        this.subCommentList = subcommentListDto;
        this.heartPostList = heartPostList;
        this.heartCommentList = heartCommentList;
        this.heartSubCommentList = heartSubCommentList;
    }

}
