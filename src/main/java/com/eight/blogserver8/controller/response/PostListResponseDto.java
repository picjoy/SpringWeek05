package com.eight.blogserver8.controller.response;

import com.eight.blogserver8.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    private Long id;
    private String title;
    private String author;
    private Long heartCount;
    private int commentCount;
    private LocalDateTime createAt;
    private LocalDateTime modifiedAt;

    //게시글 목록 response에 id, 제목, 작성자, 좋아요 개수, 대댓글 제외한 댓글 개수, 등록일, 수정일 나타내기

    public PostListResponseDto(Post post, Long heartCount){
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getMember().getNickname();
        this.heartCount = heartCount;
        this.commentCount = post.getComments().size();
        this.createAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

    }
}
