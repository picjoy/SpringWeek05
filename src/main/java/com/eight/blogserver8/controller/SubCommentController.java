package com.eight.blogserver8.controller;

import com.eight.blogserver8.controller.response.ResponseDto;
import com.eight.blogserver8.request.SubCommentRequestDto;
import com.eight.blogserver8.service.SubCommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class SubCommentController {

    private final SubCommentService subCommentService;

    @RequestMapping(value = "/api/auth/comment/subcomment", method = RequestMethod.POST)
    public ResponseDto<?> createSubComment(@RequestBody SubCommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return subCommentService.createSubComment(requestDto, request);
    }

    @RequestMapping(value = "/api/comment/subcomment/{id}", method = RequestMethod.GET)
    public ResponseDto<?> getAllSubComments(@PathVariable Long id) {
        return subCommentService.getAllSubCommentsByComment(id);
    }

    @RequestMapping(value = "/api/auth/comment/subcomment/{id}", method = RequestMethod.PUT)
    public ResponseDto<?> updateSubComment(@PathVariable Long id, @RequestBody SubCommentRequestDto requestDto,
                                        HttpServletRequest request) {
        return subCommentService.updateSubComment(id, requestDto, request);
    }

    @RequestMapping(value = "/api/auth/comment/subcomment/{id}", method = RequestMethod.DELETE)
    public ResponseDto<?> deleteSubComment(@PathVariable Long id,
                                        HttpServletRequest request) {
        return subCommentService.deleteSubComment(id, request);
    }
}
