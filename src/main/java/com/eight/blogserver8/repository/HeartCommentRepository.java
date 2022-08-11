package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.HeartComment;
import com.eight.blogserver8.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

public interface HeartCommentRepository extends JpaRepository<HeartComment, Long> {
    Long countAllByCommentId(Long commentId);

    List<HeartComment> findByMember(Member member);

    @Transactional
    void deleteByCommentId(Long commentId);
}
