package jajo.jajo_ex.dto;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class BoardRequestDto {
    private Long no;
    private String title;
    private String content;
    private int recommend;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Member member;

    public Board toEntity(){
        return Board.builder()
                .no(no)
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
