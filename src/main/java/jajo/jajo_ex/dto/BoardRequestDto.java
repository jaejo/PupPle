package jajo.jajo_ex.dto;

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
    private String files;
}
