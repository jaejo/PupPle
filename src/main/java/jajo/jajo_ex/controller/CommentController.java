package jajo.jajo_ex.controller;

import jajo.jajo_ex.SessionConst;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.CommentRequestDto;
import jajo.jajo_ex.dto.CommentResponseDto;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.BoardService;
import jajo.jajo_ex.service.CommentService;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class CommentController {
    private final CommentService commentService;
    private final BoardService boardService;
    private final MemberService memberService;

    @Autowired
    public CommentController(CommentService commentService, BoardService boardService, MemberService memberService) {
        this.commentService = commentService;
        this.boardService = boardService;
        this.memberService = memberService;
    }

    @ResponseBody
    @PostMapping("/createComment")
    public ResponseDto<?> create(@RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        System.out.println("통신 성공");
        createComment(requestDto, request);
        return ResponseDto.success(1);
    }

    @Transactional
    public ResponseDto<?> createComment(CommentRequestDto requestDto, HttpServletRequest request) {
        Member member = memberService.isPresentMember(requestDto.getMemberId());
        Board board = boardService.isPresentBoard(requestDto.getBoardNo());

        if (null == board) {
            return ResponseDto.fail("NOT_FOUND", "존재하지않는 게시글입니다.");
        }

        Comment parent = null;
        if(requestDto.getParentId() != null) {
            parent = commentService.isPresentComment(requestDto.getParentId());
            if (parent == null) {
                return ResponseDto.fail("NOT_FOUND", "존재하지 않는 댓글 id 입니다.");
            }
            if (parent.getBoard().getNo() != requestDto.getBoardNo()) {
                return ResponseDto.fail("NOT_FOUND", "부모 댓글과 자식 댓글의 게시글 번호가 다릅니다.");
            }
        }
        Comment comment = Comment.builder()
                .member(member)
                .board(board)
                .content(requestDto.getContent())
                .build();
        if (parent != null) {
            comment.updateParent(parent);
        }
        commentService.save(comment);

        CommentResponseDto commentResponseDto = null;
        if(parent != null){
            commentResponseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .author(comment.getMember().getUserId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .parentId(comment.getParent().getId())
                    .build();
        } else {
            commentResponseDto = CommentResponseDto.builder()
                    .id(comment.getId())
                    .author(comment.getMember().getUserId())
                    .content(comment.getContent())
                    .createdAt(comment.getCreatedAt())
                    .modifiedAt(comment.getModifiedAt())
                    .build();
        }

        return ResponseDto.success(commentResponseDto);
    }

    @PostMapping("/updateComment")
    @ResponseBody
    public ResponseDto<?> update(@RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.isPresentComment(requestDto.getId());
        comment.update(requestDto);
        commentService.save(comment);
        return ResponseDto.success(1);
    }

    @PostMapping("/deleteComment")
    @ResponseBody
    public ResponseDto<?> delete(@RequestBody CommentRequestDto requestDto) {
        Comment comment = commentService.isPresentComment(requestDto.getId());
        commentService.remove(comment);
        return ResponseDto.success(1);
    }

}
