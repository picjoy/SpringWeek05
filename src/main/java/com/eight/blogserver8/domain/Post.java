package com.eight.blogserver8.domain;


import com.eight.blogserver8.request.PostRequestDto;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Post extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String title;

  @Column(nullable = false)
  private String content;

  @Column
  private String imageUrl;


  @JsonManagedReference /// 무한루프 매니저 점
  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "post")
  private List<Comment> comments;

  @Column(nullable = true)
  private Long heart;


  @OneToMany(mappedBy="post", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HeartPost> heartPosts;

  public void updateHeart(Long heart) {
    this.heart = heart;
  }

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;

  public void update(PostRequestDto postRequestDto) {
    this.title = postRequestDto.getTitle();
    this.content = postRequestDto.getContent();
  }

  public void updateImage(String imageUrl){
    this.imageUrl = imageUrl;
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }

}
