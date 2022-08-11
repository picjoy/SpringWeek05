package com.eight.blogserver8.service;

import com.eight.blogserver8.controller.response.*;
import com.eight.blogserver8.domain.*;
import com.eight.blogserver8.jwt.TokenProvider;
import com.eight.blogserver8.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MypageService {



    //
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final SubCommentRepository subCommentRepository;
    private final HeartPostRepository heartPostRepository;
    private final HeartCommentRepository heartCommentRepository;
    private final HeartSubCommentRepository heartSubCommentRepository;
    private final TokenProvider tokenProvider;


    @Transactional
    public ResponseDto<?> viewMypage(HttpServletRequest request) {
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

//        String name = maybePerson.get().getName();
//        Member findMember = memberRepository.findByNickname(mypageRequestDto.getNickname()).get(); // optional. get을해서 넣어줌..

        // 내가 작성한글 모음 db에서 들고오기
        List<Post> postList = postRepository.findByMember(member);
        List<Comment> commentList = commentRepository.findByMember(member);
        List<SubComment> subCommentList = subCommentRepository.findByMember(member);


        // 내가 좋아요한 글 db에서 들고오기
        List<HeartPost> heartPostList = heartPostRepository.findByMember(member);
        List<HeartComment> heartCommentList= heartCommentRepository.findByMember(member);
        List<HeartSubComment> heartSubCommentList= heartSubCommentRepository.findByMember(member);




        MypageResponseDto mypageResponseDto = new MypageResponseDto();

        // 내가 작성한 글 모음
        List<MypagePostResponseDto> postListDto = new ArrayList<>();
        List<MypageCommentResponseDto> commentListDto = new ArrayList<>();
        List<MypageSubCommentResponseDto> subcommentListDto = new ArrayList<>();
        // 내가 좋아요한 글 모음..
        List<MypagePostResponseDto> heartPostListDto = new ArrayList<>();
        List<MypageCommentResponseDto> heartCommentListDto = new ArrayList<>();
        List<MypageSubCommentResponseDto> heartSubcommentListDto = new ArrayList<>();

        for( Post post : postList){
            postListDto.add(
                    MypagePostResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .postContent(post.getContent())
                            .imageUrl(post.getImageUrl())
                            .heart(post.getHeart())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }


        for (Comment comment : commentList){
            commentListDto.add(
                    MypageCommentResponseDto.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .heart(comment.getHeart())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }

        for(SubComment subComment : subCommentList){
            subcommentListDto.add(
                    MypageSubCommentResponseDto.builder()
                            .subCommentId(subComment.getId())
                            .subCommentContent(subComment.getContent())
                            .heart(subComment.getHeart())
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }


        for (HeartPost heartPost : heartPostList) {
            Post post = heartPost.getPost();
            heartPostListDto.add(
                    MypagePostResponseDto.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .postContent(post.getContent())
                            .imageUrl(post.getImageUrl())
                            .heart(post.getHeart())
                            .createdAt(post.getCreatedAt())
                            .modifiedAt(post.getModifiedAt())
                            .build()
            );
        }


        for (HeartComment heartComment : heartCommentList){
            Comment comment = heartComment.getComment();
            heartCommentListDto.add(
                    MypageCommentResponseDto.builder()
                            .commentId(comment.getId())
                            .commentContent(comment.getContent())
                            .heart(comment.getHeart())
                            .createdAt(comment.getCreatedAt())
                            .modifiedAt(comment.getModifiedAt())
                            .build()
            );
        }


        for(HeartSubComment heartSubComment: heartSubCommentList){
            SubComment subComment = heartSubComment.getSubComment();
            heartSubcommentListDto.add(
                    MypageSubCommentResponseDto.builder()
                            .subCommentId(subComment.getId())
                            .subCommentContent(subComment.getContent())
                            .heart(subComment.getHeart())
                            .createdAt(subComment.getCreatedAt())
                            .modifiedAt(subComment.getModifiedAt())
                            .build()
            );
        }


//        mypage.update(findMember,postList,commentList,subCommentList);// 마이페이지 객체에 업데이트
//        mypageRepository.save(mypage);   // 저장하는게 아니다.


        mypageResponseDto.update(postListDto,commentListDto,subcommentListDto,heartPostListDto,heartCommentListDto,
                heartSubcommentListDto); // 빌더 안쓰고 리스폰 만듬.

         return ResponseDto.success( mypageResponseDto );

    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
