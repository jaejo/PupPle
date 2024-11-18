package jajo.jajo_ex.controller;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/board")
public class BoardUpdateController {
    private final MemberService memberService;
    private final BoardService boardService;

    @Autowired
    public BoardUpdateController(MemberService memberService, BoardService boardService) {
        this.memberService = memberService;
        this.boardService = boardService;
    }

//    @DeleteMapping("/deleteForm")
//    public void delete(@RequestParam("no") Long no){
//        //boardService.deleteBoard(no);
//    }
    @DeleteMapping("/deleteForm")
    public ResponseDto<Integer> delete1(@RequestParam("no") Long no){
        boardService.deleteBoard(no);
        return ResponseDto.success(1);
    }
    @PostMapping("/recommend")
    @ResponseBody
    public ResponseDto<?> increase(@RequestBody BoardRequestDto requestDto) {
        Board board = boardService.isPresentBoard(requestDto.getNo());
        board.updateRecommend(requestDto);
        boardService.save(board);
        return ResponseDto.success(1);
    }
}
