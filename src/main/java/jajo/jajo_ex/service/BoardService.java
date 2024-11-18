package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.repository.BoardRepository;
import jakarta.transaction.Transactional;

import java.util.Collection;
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

    public List<Board> selectBoardList(PageDto pageDto) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.findBoardAll(pageDto);
    }
    public Board isPresentBoard(Long no) {
        return boardRepository.isPresentBoard(no);
    }

    public List<Board> searchByHint(PageDto pageDto, String hint) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByHint(pageDto, hint);
    }

    public List<Board> searchByUser(PageDto pageDto, String user) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByUser(pageDto, user);
    }

    public List<Board> searchByTitle(PageDto pageDto, String title) {
        int count = boardRepository.countAll(pageDto);

        if (count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByTitle(pageDto, title);
    }

    public List<Board> searchByRecommend(PageDto pageDto) {
        int count = boardRepository.countAll(pageDto);

        if(count == 0) return Collections.emptyList();

        pageDto.setTotalCount(count);

        return boardRepository.searchByRecommend(pageDto);
    }
}
