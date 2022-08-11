package com.eight.blogserver8.shared;

import com.eight.blogserver8.domain.Post;
import com.eight.blogserver8.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@RequiredArgsConstructor // final 멤버 변수를 자동으로 생성합니다.
@Component // 스프링이 필요 시 자동으로 생성하는 클래스 목록에 추가합니다.
@Transactional
public class Scheduler {
    private final PostRepository postRepository;

    @Scheduled(cron = "0 0 1 * * *")
    public void deletezeroPrice() {

        List<Post> postList = postRepository.findAll();
        for (int i = 0; i < postList.size(); i++) {
            Post p = postList.get(i);
            if (p.getComments().isEmpty()){

                postRepository.delete(p);
                log.info("게시글" + p.getTitle() + "이 삭제되었습니다.");

            }





        }

    }

}
