package jajo.jajo_ex.controller;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.BoardFile;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.CommentResponseDto;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.BoardFileService;
import jajo.jajo_ex.service.BoardServiceV2;
import jajo.jajo_ex.service.CommentService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class BoardGetController {
    private final BoardServiceV2 boardService;
    private final MemberService memberService;
    private final BoardFileService boardFileService;
    private final CommentService commentService;


    public BoardGetController(BoardServiceV2 boardService, MemberService memberService, BoardFileService boardFileService, CommentService commentService) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.boardFileService = boardFileService;
        this.commentService = commentService;
    }

    @GetMapping("/boards")
    public String list(@SessionAttribute(required = false, name="principal") Member principal, Model model, PageDto pageDto,
                       @RequestParam(required= false, name="boardType") BoardType boardType,
                       @RequestParam(value="hint", required = false) String hint,
                       @RequestParam(value="searchTarget", required = false) String obj) {

        model.addAttribute("boardType", boardType);


        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("boardFile", boardFileService.findAll());
        if( (hint == null || hint.isEmpty())) {
            // 추천순 검색
            if (Objects.equals(obj, "targetRecommend")) model.addAttribute("boards", boardService.searchByRecommend(pageDto, boardType));
            else model.addAttribute("boards", boardService.selectBoardList(pageDto, boardType));
        }
        else {
            // 내용기반 검색
            if (obj.equals("targetContent")) {
                model.addAttribute("boards", boardService.searchByHint(pageDto, hint, boardType));
            }
            // 유저기반 검색
            if (obj.equals("targetUser")) {
                model.addAttribute("boards", boardService.searchByUser(pageDto, hint, boardType));
            }
            // 제목기반 검색
            if (obj.equals("targetTitle")) {
                model.addAttribute("boards", boardService.searchByTitle(pageDto, hint, boardType));
            }
        }
        return "boards/boardListV2";
    }

    @Transactional
    public ResponseDto<?> getAllCommentsByBoard(Long no) {
        BoardV2 board = boardService.isPresentBoard(no);
        List<Comment> commentList = commentService.findAllByBoardV2(board);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();

        commentList.stream().forEach(c -> {
            CommentResponseDto cdto = new CommentResponseDto(c);

            if (c.getParent() != null) {
                cdto.setParentId(c.getParent().getId());
            }
            map.put(cdto.getId(), cdto);
            if (c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(cdto);
            else commentResponseDtoList.add(cdto);
        });
        return ResponseDto.success(commentResponseDtoList);
    }

    @GetMapping("/detailV2")
    public String detail(@RequestParam("no") Long no, @RequestParam("boardType") BoardType boardType,
                         @SessionAttribute(required = false, name="principal") Member principal, Model model, HttpServletRequest request) {
        if (principal != null) model.addAttribute("member", principal);

        BoardV2 board = boardService.findNo(no);
        List<Comment> comment = commentService.findBoardNo(no);
        model.addAttribute("board", board);
        model.addAttribute("delta", board.getContent());
        model.addAttribute("comments", getAllCommentsByBoard(no).getData());
        model.addAttribute("boardFile", boardFileService.findByBoard(no));
        model.addAttribute("boardType", boardType);

        return "boards/boardDetailV2";
    }

    @GetMapping("/updateBoardV2")
    public String update(@RequestParam("no") Long no, @RequestParam("boardType") String boardType,
                    @SessionAttribute(required = false, name="principal") Member principal, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        BoardV2 board = boardService.isPresentBoard(no);
        BoardFile boardFile = boardFileService.isPresentBoardFile(no);

        model.addAttribute("no", no);
        model.addAttribute("board", board);
        model.addAttribute("boardType", boardType);
        model.addAttribute("delta", board.getContent());
        model.addAttribute("boardFile", boardFile);

        return "boards/updateBoardV2";
    }

}
