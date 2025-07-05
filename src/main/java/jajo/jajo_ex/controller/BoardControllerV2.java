package jajo.jajo_ex.controller;

import jajo.jajo_ex.dto.QuillDataDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BoardControllerV2 {
    @PostMapping("/newBoardV2")
    public ResponseEntity<?> saveBoard(@RequestBody QuillDataDTO quillData) {
        String title = quillData.getTitle();
        String html = quillData.getHtml();
        Map<String, Object> delta = quillData.getDelta();
        String boardType = quillData.getBoardType();

        System.out.println("Delta: " + delta);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("boardType", boardType);

        return ResponseEntity.ok(response);
    }
}
