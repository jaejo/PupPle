package jajo.jajo_ex.controller;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardFile;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.*;
import jajo.jajo_ex.service.BoardFileService;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.CommentService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final BoardFileService boardFileService;

    @Autowired
    public BoardController(BoardService boardService, MemberService memberService, CommentService commentService, BoardFileService boardFileService) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.commentService = commentService;
        this.boardFileService = boardFileService;
    }

    @GetMapping("/newBoard")
    public String createBoardForm(@SessionAttribute(required = false, name="principal") Member principal, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        return "boards/createBoardForm";
    }
    @GetMapping("/boards")
    public String list(@SessionAttribute(required = false, name="principal") Member principal, Model model, PageDto pageDto,
                       @RequestParam(value="hint", required = false) String hint,
                       @RequestParam(value="searchTarget", required = false) String obj) {
        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("boardFile", boardFileService.findAll());
        if( (hint == null || hint.isEmpty())) {
            // 추천순 검색
            if (Objects.equals(obj, "targetRecommend")) model.addAttribute("boards", boardService.searchByRecommend(pageDto));
            else model.addAttribute("boards", boardService.selectBoardList(pageDto));
        }
        else {
            // 내용기반 검색
            if (obj.equals("targetContent")) {
                model.addAttribute("boards", boardService.searchByHint(pageDto, hint));
            }
            // 유저기반 검색
            if (obj.equals("targetUser")) {
                model.addAttribute("boards", boardService.searchByUser(pageDto, hint));
            }
            // 제목기반 검색
            if (obj.equals("targetTitle")) {
                model.addAttribute("boards", boardService.searchByTitle(pageDto, hint));
            }
        }
        return "boards/boardList";
    }

    @Transactional
    public ResponseDto<?> getAllCommentsByBoard(Long no) {
        Board board = boardService.isPresentBoard(no);
        List<Comment> commentList = commentService.findAllByBoard(board);

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

    @GetMapping("/detail")
    public String detail(@RequestParam("no") Long no, @SessionAttribute(required = false, name="principal") Member principal, Model model, HttpServletRequest request) {
        if (principal != null) model.addAttribute("member", principal);

        Board board = boardService.findNo(no);
        List<Comment> comment = commentService.findBoardNo(no);
        model.addAttribute("board", board);
        model.addAttribute("comments", getAllCommentsByBoard(no).getData());

        return "boards/boardDetail";
    }
    @PostMapping("/updateBoard")
    public String update(@RequestParam("no") String no,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @SessionAttribute(required = false, name="principal") Member principal,
                         Model model) {
        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("no", no);
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        return "boards/updateBoard";
    }

    @PostMapping("/updateForm")
    @Transactional
    public String updateForm(@SessionAttribute(required = false, name="principal") Member principal, BoardForm boardForm, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        // 글을 수정하기 위해 영속성 콘텍스트에 저장
        Board board = boardService.findNo(boardForm.getNo());
        board.setTitle(boardForm.getTitle());
        board.setContent(boardForm.getContent());
        boardService.save(board);
        return "redirect:/boards";
    }

    @PostMapping("/newBoard")
    @Transactional
    public String post(@SessionAttribute(required=false, name="principal") Member principal, Model model,
                               @RequestParam(required=false, name="file") MultipartFile[] file, BoardForm form) throws IOException {
        if (principal != null) model.addAttribute("member", principal);
        Member member = memberService.findById(principal);
        String allFile = "";
        ZonedDateTime zdateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formatedNow = zdateTime.format(formatter);
        // 저장될 경로 지정
        String FileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";

        for (MultipartFile f : file) {
            if (!f.isEmpty()) {
                String userFileName = f.getOriginalFilename();
                String fileExtension = userFileName.substring(userFileName.lastIndexOf(".") + 1);
                userFileName = userFileName.substring(0, userFileName.lastIndexOf("."));
                String uploadFileName = formatedNow + "_" + userFileName + "." + fileExtension;
                allFile += uploadFileName + ", ";
                File saveFile = new File(FileDir, uploadFileName);
                f.transferTo(saveFile);
            }
        }

        Board board = Board.builder()
                .member(member)
                .title(form.getTitle())
                .content(form.getContent())
                .boardType(form.getBoardType())
                .build();

        boardService.save(board);

        BoardFile boardFile = BoardFile.builder()
                .board(board)
                .fileName(allFile)
                .build();

        boardFileService.save(boardFile);

        return "redirect:/boards";
    }

}
