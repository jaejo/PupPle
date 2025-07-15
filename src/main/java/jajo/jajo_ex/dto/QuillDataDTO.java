package jajo.jajo_ex.dto;

import jajo.jajo_ex.BoardType;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class QuillDataDTO {
    private String title;
    private Map<String, Object> delta;
    private BoardType boardType;
    private int recommend;

//    public String getTitle() { return title; }
//    public void setTitle(String title) {this.title = title; }
//
//    public Map<String, Object> getDelta() { return delta; }
//    public void setDelta(Map<String, Object> delta){ this.delta = delta; }

//    public String getBoardType() { return boardType; }
//    public void setBoardType(String boardType) { this.boardType = boardType; }
}
