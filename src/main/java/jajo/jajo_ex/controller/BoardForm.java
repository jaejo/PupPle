package jajo.jajo_ex.controller;

import jajo.jajo_ex.BoardType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Controller;

@Controller
@Getter
@Setter
public class BoardForm {
    private Long no;
    private String title;
    private String content;
    private BoardType boardType;
}
