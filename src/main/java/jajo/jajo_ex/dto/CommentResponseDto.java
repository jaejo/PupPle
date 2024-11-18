package jajo.jajo_ex.dto;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    private Long id;
    private String content;
    private String author;
//    private Board board;
    private Long parentId;
    private Long boardNo;
//    private Comment parent;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private List<CommentResponseDto> children = new ArrayList<>();

    @Builder
    public CommentResponseDto(Comment c) {
        this.id = c.getId();
        this.content = c.getContent();
        this.author = c.getMember().getUserId();
        this.createdAt = c.getCreatedAt();
        this.modifiedAt = c.getModifiedAt();
        this.parentId = getParentId();
        this.boardNo = c.getBoard().getNo();
    }
}
