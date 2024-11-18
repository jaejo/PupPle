package jajo.jajo_ex.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "MEMBER_ID")
    private Long id;

    private String name;

    private String userId;

    private String userPw;

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<Board>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<Comment>();

    public void addBoard(Board board){
        this.boards.add(board);
        board.setMember(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setMember(this);
    }
}
