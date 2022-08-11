package com.eight.blogserver8.controller;

import com.eight.blogserver8.request.PostRequestDto;
import com.eight.blogserver8.controller.response.ResponseDto;
import com.eight.blogserver8.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @RequestMapping(value = "/api/auth/post", method = RequestMethod.POST) // 게시글작성 + 이미지업로드
    public ResponseDto<?> createPost(@RequestPart(value = "post") PostRequestDto requestDto,@RequestPart(value = "image") MultipartFile multipartFile,
                                     HttpServletRequest request) throws IOException {
        return postService.createPost(requestDto,multipartFile,request);
    }//게시글 작성할때 body-> form-date 누르고 (key,value) 기입
    // (post,{
    //    "title": "title",
    //    "content": "content"
    //}) -> content-type application/json 선택
    //(image(file 선택), local에 있는 이미지파일)

    @RequestMapping(value = "/api/post/{id}", method = RequestMethod.GET)
    public ResponseDto<?> getPost(@PathVariable Long id) {
        return postService.getPost(id);
    }

    @RequestMapping(value = "/api/post", method = RequestMethod.GET)
    public ResponseDto<?> getAllPosts() {
        return postService.getAllPost();
    }

    @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updatePost(@PathVariable Long id, @RequestBody PostRequestDto postRequestDto,
                                     HttpServletRequest request) {
        return postService.updatePost(id, postRequestDto, request);
    }

    @RequestMapping(value = "/api/auth/post/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deletePost(@PathVariable Long id,
                                     HttpServletRequest request) {
        return postService.deletePost(id, request);
    }

    @RequestMapping(value = "/api/auth/post/image/{id}", method = RequestMethod.PATCH) //이미지만 수정
    public ResponseDto<?> updateImage(@PathVariable Long id, @RequestPart(value = "image") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
        return  postService.updateImage(id,multipartFile,request);
    }// 이미지만 수정

}