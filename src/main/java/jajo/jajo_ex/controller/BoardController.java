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
    public String createBoardForm(@SessionAttribute(required = false, name="principal") Member principal, Model model,
                                  @RequestParam(name="boardType") BoardType boardType) {
        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("boardType", boardType);
        return "boards/createBoardForm";
    }

    @GetMapping("/newBoardV2")
    public String createBoardFormV2(@SessionAttribute(required = false, name="principal") Member principal, Model model,
                                  @RequestParam(name="boardType") BoardType boardType) {
        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("boardType", boardType);
        return "boards/createBoardFormV2";
    }

//    @GetMapping("/boards")
//    public String list(@SessionAttribute(required = false, name="principal") Member principal, Model model, PageDto pageDto,
//                       @RequestParam(required= false, name="boardType") BoardType boardType,
//                       @RequestParam(value="hint", required = false) String hint,
//                       @RequestParam(value="searchTarget", required = false) String obj) {
//
//        model.addAttribute("boardType", boardType);
//
//
//        if (principal != null) model.addAttribute("member", principal);
//        model.addAttribute("boardFile", boardFileService.findAll());
//        if( (hint == null || hint.isEmpty())) {
//            // 추천순 검색
//            if (Objects.equals(obj, "targetRecommend")) model.addAttribute("boards", boardService.searchByRecommend(pageDto, boardType));
//            else model.addAttribute("boards", boardService.selectBoardList(pageDto, boardType));
//        }
//        else {
//            // 내용기반 검색
//            if (obj.equals("targetContent")) {
//                model.addAttribute("boards", boardService.searchByHint(pageDto, hint, boardType));
//            }
//            // 유저기반 검색
//            if (obj.equals("targetUser")) {
//                model.addAttribute("boards", boardService.searchByUser(pageDto, hint, boardType));
//            }
//            // 제목기반 검색
//            if (obj.equals("targetTitle")) {
//                model.addAttribute("boards", boardService.searchByTitle(pageDto, hint, boardType));
//            }
//        }
//        return "boards/boardList";
//    }

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

//    @GetMapping("/detail")
//    public String detail(@RequestParam("no") Long no, @RequestParam("boardType") BoardType boardType,
//                         @SessionAttribute(required = false, name="principal") Member principal, Model model, HttpServletRequest request) {
//        if (principal != null) model.addAttribute("member", principal);
//
//        Board board = boardService.findNo(no);
//        List<Comment> comment = commentService.findBoardNo(no);
//        model.addAttribute("board", board);
//        model.addAttribute("comments", getAllCommentsByBoard(no).getData());
//        model.addAttribute("boardFile", boardFileService.findByBoard(no));
//        model.addAttribute("boardType", boardType);
//
//        return "boards/boardDetail";
//    }
    @PostMapping("/updateBoard")
    public String update(@RequestParam("no") Long no,
                         @RequestParam("title") String title,
                         @RequestParam("content") String content,
                         @RequestParam("boardType") BoardType boardType,
                         @SessionAttribute(required = false, name="principal") Member principal,
                         Model model) {
        if (principal != null) model.addAttribute("member", principal);
        model.addAttribute("no", no);
        model.addAttribute("title", title);
        model.addAttribute("content", content);
        model.addAttribute("boardFile", boardFileService.findByBoard(no));
        model.addAttribute("boardType", boardType);
        return "boards/updateBoard";
    }

//    //formData로 전송하여 @RequestPart로 이미지와 Dto를 분리
//    @PostMapping("/updateForm")
//    @ResponseBody
//    @Transactional
//    public ResponseDto<?> updateForm(@SessionAttribute(required = false, name="principal") Member principal,
//                                    @RequestPart(value="info") BoardRequestDto boardRequestDto, Model model,
//                                     @RequestPart(value="file", required = false) MultipartFile[] file) throws IOException{
//        if (principal != null) model.addAttribute("member", principal);
//
//        //체크한 파일 삭제
//        String removeFile = boardRequestDto.getDeleteFileName();
//
//        for (String removeFileName : removeFile.split(",")) {
//            removeFileName = removeFileName.trim();
//            if (removeFileName != "") {
//                String DeleteFileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
//                DeleteFileDir += boardRequestDto.getBoardType() + "\\" + removeFileName;
//                File DeleteFile = new File(DeleteFileDir);
//                if (DeleteFile.delete()) {
//                   System.out.println(removeFileName + ": 삭제 완료");
//                }
//            }
//        }
//
//        ZonedDateTime zdateTime = ZonedDateTime.now();
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
//        String formatedNow = zdateTime.format(formatter);
//        String FileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
//        FileDir += boardRequestDto.getBoardType() + "\\";
//
//        //기존의 이미지 파일
//        String allFile = boardRequestDto.getRemainFileName();
//
//        //새로 들어온 파일 생성 및 DB 저장
//        if (file != null) {
//            for (MultipartFile f : file) {
//                if (!f.isEmpty()) {
//                    String userFileName = f.getOriginalFilename();
//                    String fileExtension = userFileName.substring(userFileName.lastIndexOf(".") + 1);
//                    userFileName = userFileName.substring(0, userFileName.lastIndexOf("."));
//                    String uploadFileName = formatedNow + "_" + userFileName + "." + fileExtension;
//                    allFile += uploadFileName + ", ";
//                    File saveFile = new File(FileDir, uploadFileName);
//                    f.transferTo(saveFile);
//                }
//            }
//        }
//
//        Board board = boardService.isPresentBoard(boardRequestDto.getNo());
//
//        board.update(boardRequestDto);
//
//        boardFileService.updateFileName(boardRequestDto.getNo(), allFile);
//
//        boardService.save(board);
//
//        return ResponseDto.success(board.getBoardType());
//    }

    @PostMapping("/newBoard")
    @ResponseBody
    @Transactional
    public ResponseDto<?> post(@SessionAttribute(required=false, name="principal") Member principal, Model model,
                       @RequestPart(value="info") BoardRequestDto boardRequestDto,
                       @RequestPart(value="file", required = false) MultipartFile[] file) throws IOException {
        if (principal != null) model.addAttribute("member", principal);

        Member member = memberService.findById(principal);
        String allFile = "";
        ZonedDateTime zdateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formatedNow = zdateTime.format(formatter);
        // 저장될 경로 지정
        String FileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
        FileDir += boardRequestDto.getBoardType() + "\\";
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
                .title(boardRequestDto.getTitle())
                .content(boardRequestDto.getContent())
                .boardType(boardRequestDto.getBoardType())
                .build();

        boardService.save(board);

        BoardFile boardFile = BoardFile.builder()
                .board(board)
                .fileName(allFile)
                .build();

        boardFileService.save(boardFile);

        return ResponseDto.success(boardRequestDto.getBoardType());
    }

    //formData로 전송하여 @RequestPart로 이미지와 Dto를 분리
    @PostMapping("/updateForm")
    @ResponseBody
    @Transactional
    public ResponseDto<?> updateForm(@SessionAttribute(required = false, name="principal") Member principal,
                                     @RequestPart(value="info") BoardRequestDto boardRequestDto, Model model,
                                     @RequestPart(value="file", required = false) MultipartFile[] file) throws IOException{
        if (principal != null) model.addAttribute("member", principal);

        List<BoardFile> boardFile = boardFileService.findByBoard(boardRequestDto.getNo());

        String removeFile = boardFile.get(0).getFileName();

        for (String removeFileName : removeFile.split(",")) {
            removeFileName = removeFileName.trim();
            if (removeFileName != "") {
                String DeleteFileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
                DeleteFileDir += boardRequestDto.getBoardType() + "\\" + removeFileName;
                File DeleteFile = new File(DeleteFileDir);
                if (DeleteFile.delete()) {
                    System.out.println(removeFileName + ": 삭제 완료");
                }
            }
        }

        //체크한 파일 삭제
//        String removeFile = boardRequestDto.getDeleteFileName();
//
//        for (String removeFileName : removeFile.split(",")) {
//            removeFileName = removeFileName.trim();
//            if (removeFileName != "") {
//                String DeleteFileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
//                DeleteFileDir += boardRequestDto.getBoardType() + "\\" + removeFileName;
//                File DeleteFile = new File(DeleteFileDir);
//                if (DeleteFile.delete()) {
//                    System.out.println(removeFileName + ": 삭제 완료");
//                }
//            }
//        }

        ZonedDateTime zdateTime = ZonedDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
        String formatedNow = zdateTime.format(formatter);
        String FileDir = "C:\\Users\\LEEJAEJOON\\Documents\\PupPle\\src\\main\\resources\\static\\uploadImage\\";
        FileDir += boardRequestDto.getBoardType() + "\\";

        //DB에 저장될 이미지 파일 이름들
        String allFile = "";

        //새로 들어온 파일 생성 및 DB 저장
        if (file != null) {
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
        }

        Board board = boardService.isPresentBoard(boardRequestDto.getNo());

        board.update(boardRequestDto);

        boardFileService.updateFileName(boardRequestDto.getNo(), allFile);

        boardService.save(board);

        return ResponseDto.success(board.getBoardType());

    }
}
