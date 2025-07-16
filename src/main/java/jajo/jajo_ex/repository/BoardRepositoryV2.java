package jajo.jajo_ex.repository;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.dto.PageDto;

import java.util.List;

public interface BoardRepositoryV2 {
    BoardV2 save(BoardV2 board);
    void increaseRecommend(Long no);
    List<BoardV2> findUserBoardAll(String userId);
    List<BoardV2> findAll();
    List<BoardV2> findBoardAll(PageDto pageDto, BoardType boardType);
    BoardV2 findByNo(Long no);
    void delete(Long no);
    int countAll(PageDto pageDto, BoardType boardType);
    BoardV2 isPresentBoard(Long no);
    List<BoardV2> searchByHint(PageDto pageDto, String hint, BoardType boardType);
    List<BoardV2> searchByUser(PageDto pageDto, String user, BoardType boardType);
    List<BoardV2> searchByTitle(PageDto pageDto, String title, BoardType boardType);
    List<BoardV2> searchByRecommend(PageDto pageDto, BoardType boardType);
    List<BoardV2> findCategory(BoardType category);
}
