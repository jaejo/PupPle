package jajo.jajo_ex.domain;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.Timestamped;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.CommentRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Board extends Timestamped {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "BOARD_NO")
    private Long no;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy="board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFiles = new ArrayList<BoardFile>();


    private String title;
    private String content;
    private int recommend;

    @Column(name = "category")
    private BoardType boardType;

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBoard(this);
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.title = boardRequestDto.getTitle();
        this.content = boardRequestDto.getContent();
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

    public void updateRecommend(BoardRequestDto boardRequestDto) {
        this.recommend = boardRequestDto.getRecommend() + 1;
    }

}
