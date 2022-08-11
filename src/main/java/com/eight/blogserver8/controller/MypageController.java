package com.eight.blogserver8.controller;



import com.eight.blogserver8.controller.response.ResponseDto;
import com.eight.blogserver8.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

@Validated
@RequiredArgsConstructor
@RestController
public class MypageController {

    private final MypageService mypageService;

    @RequestMapping(value ="/api/auth/mypage", method = RequestMethod.POST)
    public ResponseDto<?> viewMypage(HttpServletRequest request){
        return mypageService.viewMypage(request);
    }


}
