package jajo.jajo_ex.dto;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Builder
public class CommentRequestDto {
    private Long id;
    private String content;
    private String author;
    private Board board;
    private Long memberId;
    private String userId;
    private Long parentId;
    private Long boardNo;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
