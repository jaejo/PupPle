package jajo.jajo_ex.dto;

import java.util.Map;

public class QuillDataDTO {
    private String title;
    private String html;
    private Map<String, Object> delta;
    private String boardType;

    public String getTitle() { return title; }
    public void setTitle(String title) {this.title = title; }

    public String getHtml() { return html; }
    public void setHtml(String html) { this.html = html; }

    public Map<String, Object> getDelta() { return delta; }
    public void setDelta(Map<String, Object> delta){ this.delta = delta; }

    public String getBoardType() { return boardType; }
    public void setBoardType(String boardType) { this.boardType = boardType; }
}
