package jajo.jajo_ex.service;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.repository.BoardRepository;
import jajo.jajo_ex.repository.BoardRepositoryV2;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

public class BoardServiceV2 {
    private final BoardRepositoryV2 boardRepository;

    public BoardServiceV2(BoardRepositoryV2 boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long save(BoardV2 board) {
        boardRepository.save(board);
        return board.getNo();
    }

    @Transactional
    public void increaseRecommend(Long no) {
        boardRepository.increaseRecommend(no);
    }

    public List<BoardV2> findBoards() {
        return boardRepository.findAll();
    }

    public BoardV2 findNo(Long no) {
        return boardRepository.findByNo(no);
    }
    @Transactional
    public void deleteBoard(Long no) {
        boardRepository.delete(no);
    }

    public List<BoardV2> selectBoardList(PageDto pageDto, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.findBoardAll(pageDto, boardType);
    }
    public BoardV2 isPresentBoard(Long no) {
        return boardRepository.isPresentBoard(no);
    }

    public List<BoardV2> searchByHint(PageDto pageDto, String hint, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByHint(pageDto, hint, boardType);
    }

    public List<BoardV2> searchByUser(PageDto pageDto, String user, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByUser(pageDto, user, boardType);
    }

    public List<BoardV2> searchByTitle(PageDto pageDto, String title, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByTitle(pageDto, title, boardType);
    }

    public List<BoardV2> searchByRecommend(PageDto pageDto, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if(count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByRecommend(pageDto, boardType);
    }

    public List<BoardV2> findCategory(BoardType category) {
        return boardRepository.findCategory(category);
    }
}
