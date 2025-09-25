package jajo.jajo_ex.domain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.Timestamped;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class BoardV2 extends Timestamped {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "BOARD2_NO")
    private Long no;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy="boardV2", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<Comment>();

    @OneToMany(mappedBy = "boardV2", cascade = CascadeType.REMOVE, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<BoardFile> boardFiles = new ArrayList<BoardFile>();


    private String title;
    @Lob
    private String content;
    private int recommend;

    @Column(name = "category")
    private BoardType boardType;

    @Transient
    private Map<String, Object> delta;

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setBoardV2(this);
    }

    public void update() throws JsonProcessingException {
        this.content = mapper.writeValueAsString(delta);
    }

    public boolean validateMember(Member member) {
        return !this.member.equals(member);
    }

//    content(delta) 값이 null로 바뀜
//    public void updateRecommend(QuillDataDTO quillData) {
//        this.recommend = quillData.getRecommend() + 1;
//    }

}
