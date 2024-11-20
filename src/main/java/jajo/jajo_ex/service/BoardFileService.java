package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.BoardFile;
import jajo.jajo_ex.repository.BoardFileRepository;

public class BoardFileService {
    private final BoardFileRepository boardFileRepository;

    public BoardFileService(BoardFileRepository boardFileRepository) {
        this.boardFileRepository = boardFileRepository;
    }

    public Long save(BoardFile boardFile) {
        boardFileRepository.save(boardFile);
        return boardFile.getId();
    }
}
