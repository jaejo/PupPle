package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.BoardFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardFileRepository {
    BoardFile save(BoardFile boardFile);
    List<BoardFile> findAll();
    List<BoardFile> findByBoard(Long no);
    List<BoardFile> findByBoardV2(Long no);
    int updateFileName(Long no, String fileName);
}
