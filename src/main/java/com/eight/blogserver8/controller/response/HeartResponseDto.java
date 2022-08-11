package com.eight.blogserver8.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class HeartResponseDto {

    private Long id;
    private String title;
    private String content;
    private String author;
    private List<CommentResponseDto> commentResponseDtoList;
    private Long heart;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;



}
