package jajo.jajo_ex.dto;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Member;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
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
    private BoardType boardType;
//    private String deleteFileName;
//    private String remainFileName;

    public Board toEntity(){
        return Board.builder()
                .no(no)
                .title(title)
                .content(content)
                .member(member)
                .build();
    }
}
