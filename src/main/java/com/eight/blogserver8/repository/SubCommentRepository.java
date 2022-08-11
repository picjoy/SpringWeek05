package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.Comment;
import com.eight.blogserver8.domain.Member;
import com.eight.blogserver8.domain.SubComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface SubCommentRepository extends JpaRepository<SubComment, Long> {
    List<SubComment> findAllByComment(Comment comment);
    List<SubComment> findByMember(Member member);

}