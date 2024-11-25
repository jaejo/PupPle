package jajo.jajo_ex.repository;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.PageDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository {
    Board save(Board board);
    List<Board> findUserBoardAll(String userId);
    List<Board> findAll();
    List<Board> findBoardAll(PageDto pageDto, BoardType boardType);
    Board findByNo(Long no);
    void delete(Long no);
    int countAll(PageDto pageDto, BoardType boardType);
    Board isPresentBoard(Long no);
    List<Board> searchByHint(PageDto pageDto, String hint, BoardType boardType);
    List<Board> searchByUser(PageDto pageDto, String user, BoardType boardType);
    List<Board> searchByTitle(PageDto pageDto, String title, BoardType boardType);
    List<Board> searchByRecommend(PageDto pageDto, BoardType boardType);
    List<Board> findCategory(BoardType category);
}
