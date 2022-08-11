package com.eight.blogserver8.repository;


import com.eight.blogserver8.domain.Comment;
import com.eight.blogserver8.domain.Member;
import com.eight.blogserver8.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findAllByPost(Post post);
  List<Comment> findByMember(Member member);
}
