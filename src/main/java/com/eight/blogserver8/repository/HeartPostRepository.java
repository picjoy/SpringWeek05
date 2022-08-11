package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartPost;
import com.eight.blogserver8.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HeartPostRepository extends JpaRepository<HeartPost, Long> {

    Long countAllByPostId(Long postId);  // 포스트 아이디를 인식하는 이유가 규칙인지???? ORM에서 자동으로 맵핑할때
                                        // db상 colum 값은 POST_ID 인데  변수명 postID이 어째서 POST_ID를 찰떡같이 들고올까??
                                     //  그 이유가 입력할때 member.getid(), post.getid()인데 이 값이  colum과 연게되어있나??
    Long countByMemberIdAndPostId(Long memberId, Long postId);

    List<HeartPost> findByMember(Member member);




    @Transactional
    void deleteByPostId(Long postId);

}
