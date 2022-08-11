package com.eight.blogserver8.domain;


import javax.persistence.*;
import java.util.List;

@Entity
public class Mypage {

    @Id
    @Column(name = "mypage_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @JoinColumn(name = "member_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)   /// Mypage에서 뒤에를 불러온다~~
    @OneToOne(fetch = FetchType.LAZY)  // Many to One에서 변경시 이상 없음..
    private Member member;


    @JoinColumn(name = "post_id", nullable = false)
    @ManyToMany(fetch = FetchType.LAZY) // 물어볼꺼임 ... ONE은  왜 안됨??
//    @OneToMany(fetch = FetchType.LAZY)  // mapped by post를 지정해주지 않아서 .
    private List<Post> postList;


    @JoinColumn(name = "comment_id", nullable = false)
//    @OneToMany(fetch = FetchType.LAZY)
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Comment> commentList;


    @JoinColumn(name = "subcomment_id", nullable = false)
    @ManyToMany(fetch = FetchType.LAZY)
//    @OneToMany(fetch = FetchType.LAZY)
    private List<SubComment> subCommentList;



//    @JoinColumn(name = "heart_id", nullable = false)
//    @ManyToOne(fetch = FetchType.LAZY)
//    private Heart heart;


    public void update(Member member,
            List<Post> postListDto,
            List<Comment> commentListDto,
            List<SubComment> subcommentListDto){
        this.member = member;
        this.postList = postListDto;
        this.commentList = commentListDto;
        this.subCommentList = subcommentListDto;

    }



}
