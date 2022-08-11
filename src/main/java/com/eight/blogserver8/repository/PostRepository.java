package com.eight.blogserver8.repository;

import com.eight.blogserver8.domain.Member;
import com.eight.blogserver8.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {
  List<Post> findAllByOrderByModifiedAtDesc();
  List<Post> findByMember(Member member);
}
