package jajo.jajo_ex.dto;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.BoardV2;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuillDataDTO {
    private Long no;
    private String title;
    private Map<String, Object> delta;
    private BoardType boardType;
    private int recommend;

}
