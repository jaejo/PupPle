package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.BoardFile;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

public class JpaBoardFileRepository implements BoardFileRepository {
    private final EntityManager em;

    public JpaBoardFileRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public BoardFile save(BoardFile boardFile) {
        em.persist(boardFile);
        return boardFile;
    }

    @Override
    public List<BoardFile> findAll() {
        return em.createQuery("select f from BoardFile f", BoardFile.class)
                .getResultList();
    }
}
