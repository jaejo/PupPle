package jajo.jajo_ex.controller;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardFile;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.BoardFileService;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@RequestMapping("/board")
public class BoardUpdateController {
    private final MemberService memberService;
    private final BoardService boardService;
    private final BoardFileService boardFileService;

    @Autowired
    public BoardUpdateController(MemberService memberService, BoardService boardService, BoardFileService boardFileService) {
        this.memberService = memberService;
        this.boardService = boardService;
        this.boardFileService = boardFileService;
    }

//    @DeleteMapping("/deleteForm")
//    public void delete(@RequestParam("no") Long no){
//        //boardService.deleteBoard(no);
//    }
    @DeleteMapping("/deleteForm")
    public ResponseDto<Integer> deleteOne(@RequestParam("no") Long no, @RequestParam("boardType") BoardType boardType){

        List<BoardFile> boardFile = boardFileService.findByBoard(no);

        String removeFile = boardFile.get(0).getFileName();
        for (String removeFileName : removeFile.split(",")) {
            removeFileName = removeFileName.trim();
            if (removeFileName != "") {
                String DeleteFileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
                DeleteFileDir += boardType + "\\" + removeFileName;
                File DeleteFile = new File(DeleteFileDir);
                if (DeleteFile.delete()) {
                    System.out.println(removeFileName + ": 삭제 완료");
                }
            }
        }
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
