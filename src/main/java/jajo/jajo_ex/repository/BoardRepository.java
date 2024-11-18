package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.dto.PageDto;

import java.util.List;

public interface BoardRepository {
    Board save(Board board);
    List<Board> findUserBoardAll(String userId);
    List<Board> findAll();
    List<Board> findBoardAll(PageDto pageDto);
    Board findByNo(Long no);
    void delete(Long no);
    int countAll(PageDto pageDto);
    Board isPresentBoard(Long no);
    List<Board> searchByHint(PageDto pageDto, String hint);
    List<Board> searchByUser(PageDto pageDto, String user);
    List<Board> searchByTitle(PageDto pageDto, String title);
    List<Board> searchByRecommend(PageDto pageDto);
}
