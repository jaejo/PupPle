package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository {
    Comment save(Comment comment);
    List<Comment> findAll();
    List<Comment> findBoardNo(Long no);
    Comment isPresentComment(Long id);
    Comment remove(Long id);
    List<Comment> findAllByBoard(Board board);
}
