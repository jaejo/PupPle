package jajo.jajo_ex.service;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.repository.BoardRepository;
import jakarta.transaction.Transactional;

import java.util.Collections;
import java.util.List;

public class BoardService {
    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public Long save(Board board) {
        boardRepository.save(board);
        return board.getNo();
    }

    public List<Board> findBoards() {
        return boardRepository.findAll();
    }

    public Board findNo(Long no) {
        return boardRepository.findByNo(no);
    }
    @Transactional
    public void deleteBoard(Long no) {
        boardRepository.delete(no);
    }

    public List<Board> selectBoardList(PageDto pageDto, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.findBoardAll(pageDto, boardType);
    }
    public Board isPresentBoard(Long no) {
        return boardRepository.isPresentBoard(no);
    }

    public List<Board> searchByHint(PageDto pageDto, String hint, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByHint(pageDto, hint, boardType);
    }

    public List<Board> searchByUser(PageDto pageDto, String user, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByUser(pageDto, user, boardType);
    }

    public List<Board> searchByTitle(PageDto pageDto, String title, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByTitle(pageDto, title, boardType);
    }

    public List<Board> searchByRecommend(PageDto pageDto, BoardType boardType) {
        int count = boardRepository.countAll(pageDto, boardType);

        if(count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByRecommend(pageDto, boardType);
    }

    public List<Board> findCategory(BoardType category) {
        return boardRepository.findCategory(category);
    }
}
