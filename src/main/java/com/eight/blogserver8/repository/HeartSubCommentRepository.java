package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartSubComment;
import com.eight.blogserver8.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HeartSubCommentRepository extends JpaRepository<HeartSubComment, Long> {
    Long countAllBySubCommentId(Long commentId);

    List<HeartSubComment> findByMember(Member member);

    @Transactional
    void deleteBySubCommentId(Long commentId);
}
