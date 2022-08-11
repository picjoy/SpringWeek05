package com.eight.blogserver8.domain;


import com.eight.blogserver8.request.CommentRequestDto;

import com.fasterxml.jackson.annotation.JsonBackReference;


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

public class Comment extends Timestamped {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JoinColumn(name = "member_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)
  private Member member;


  @JsonBackReference
 // 무한루프 해결 (참조점)

  @JoinColumn(name = "post_id", nullable = false)
  @ManyToOne(fetch = FetchType.LAZY)  // 다대일
  private Post post;

  @Column(nullable = false)
  private String content;

  @Column(nullable = true)
  @Builder.Default private Long heart = 0l;

  public void updateHeart(Long heart) {
    this.heart = heart;
  }

  @OneToMany(mappedBy = "comment" ,fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
  private List<HeartComment> heartComments ;


  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "comment")
  private List<SubComment> subComments;

  public void update(CommentRequestDto commentRequestDto) {
    this.content = commentRequestDto.getContent();
  }

  public boolean validateMember(Member member) {
    return !this.member.equals(member);
  }
}
