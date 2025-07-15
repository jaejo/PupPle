package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.repository.CommentRepository;
import jakarta.transaction.Transactional;

import java.util.List;

public class CommentService {
    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }
    @Transactional
    public Long save(Comment comment) {
        commentRepository.save(comment);
        return comment.getId();
    }

    public List<Comment> findComments() {
        return commentRepository.findAll();
    }

    public List<Comment> findBoardNo(Long no) {
        return commentRepository.findBoardNo(no);
    }

    public Comment isPresentComment(Long id) {
        return commentRepository.isPresentComment(id);
    }

    public void remove(Comment comment) {
        commentRepository.remove(comment.getId());
    }

    public List<Comment> findAllByBoard(Board board) {
        return commentRepository.findAllByBoard(board);
    }

    public List<Comment> findAllByBoardV2(BoardV2 board) { return commentRepository.findAllByBoardV2(board); }
}
