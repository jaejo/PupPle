package jajo.jajo_ex.controller;

import jajo.jajo_ex.SessionConst;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.*;
import jajo.jajo_ex.repository.BoardRepository;
import jajo.jajo_ex.repository.MemberRepository;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.CommentService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
public class BoardController {
    private final BoardService boardService;
    private final MemberService memberService;
    private final CommentService commentService;

    @Autowired
    public BoardController(BoardService boardService, MemberService memberService, CommentService commentService) {
        this.boardService = boardService;
        this.memberService = memberService;
        this.commentService = commentService;
    }

    @GetMapping("/newBoard")
    public String createBoardForm(@SessionAttribute(required = false, name="principal") Member principal, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        return "boards/createBoardForm";
    }
    @PostMapping("/newBoard")
    @Transactional
    public String create(@SessionAttribute(required = false, name="principal") Member principal, BoardForm form, Model model) {
        if (principal != null) model.addAttribute("member", principal);

        Board board = new Board();

        board.setTitle(form.getTitle());
        board.setContent(form.getContent());

        boardService.save(board);
        Member member1 = memberService.findById(principal);

        board.setMember(member1);
        member1.getBoards().add(board);
        memberService.join(member1);

        return "redirect:/boards";

    }
    @GetMapping("/boards")
    public String list(@SessionAttribute(required = false, name="principal") Member principal, Model model, PageDto pageDto,
                       @RequestParam(value="user", required = false) String user,
                       @RequestParam(value="hint", required = false) String hint,
                       @RequestParam(value="title", required = false) String title,
                       @RequestParam(value="searchTarget", required = false) String obj) {
        System.out.println(user);
        if (principal != null) model.addAttribute("member", principal);
        if( (hint == null || hint.isEmpty())
                && (user == null || user.isEmpty())
                && (title == null || title.isEmpty())) {
            if (Objects.equals(obj, "targetRecommend")) model.addAttribute("boards", boardService.searchByRecommend(pageDto));
            else model.addAttribute("boards", boardService.selectBoardList(pageDto));
        }
        else {
            if (!hint.isEmpty()) {
                model.addAttribute("boards", boardService.searchByHint(pageDto, hint));
            }
            if (!user.isEmpty()) {
                model.addAttribute("boards", boardService.searchByUser(pageDto, user));
            }
            if (!title.isEmpty()) {
                model.addAttribute("boards", boardService.searchByTitle(pageDto, title));
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

    @PostMapping("/posting")
    @ResponseBody
    @Transactional
    public ResponseDto<?> post(@SessionAttribute(required=false, name="principal") Member principal, Model model,
                               BoardForm form) {
        if (principal != null) model.addAttribute("member", principal);

        Member member = memberService.findById(principal);

        ZonedDateTime zdateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formatedNow = zdateTime.format(formatter);
        String[] files = form.getFiles().split("\n");
        for (int i = 0; i < files.length; i++) {
            try {
                BufferedImage image = new BufferedImage(300, 300, BufferedImage.TYPE_INT_RGB);
                File file = new File("C:\\Users\\arki\\Desktop\\study\\jajo-ex\\build\\resources\\main\\static\\upload\\" + files[i]);
                ImageIO.write(image, "jpg", file);
            } catch (IOException e) {
                System.out.println("이미지 저장 중 오류 발생");
            }
        }

//
//        Board board = Board.builder()
//                .member(member)
//                .title(form.getTitle())
//                .content(form.getContent())
//                .files(form.getFiles())
//                .build();
//        boardService.save(board);

        return ResponseDto.success(1);
    }
}
