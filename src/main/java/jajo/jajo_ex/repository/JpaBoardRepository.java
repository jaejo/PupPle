package jajo.jajo_ex.repository;

import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.dto.BoardRequestDto;
import jajo.jajo_ex.dto.PageDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

import java.util.List;

public class JpaBoardRepository implements BoardRepository {

    private final EntityManager em;

    public JpaBoardRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Board save(Board board) {
        em.persist(board);
        return board;
    }

    @Override
    public List<Board> findUserBoardAll(String userId) {
        List<Board> result = em.createQuery("select b from Member m, Board b where m.userId = :userId", Board.class)
                .setParameter("userId", userId)
                .getResultList();
        return result;
    }

    @Override
    public List<Board> findAll() {
        List<Board> result = em.createQuery("select b from Board b", Board.class)
                .getResultList();
        return result;
    }

    @Override
    public List<Board> findBoardAll(PageDto pageDto, BoardType boardType) {
        List<Board> result = em.createQuery("select b from Board b where b.boardType = :category order by b.no desc", Board.class)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        return result;
    }

    @Override
    public Board findByNo(Long no) {
        Board result = em.find(Board.class, no);
        return result;
    }

    @Override
    public void delete(Long no) {
//        String jpql = "delete from Board b where b.no =:no";
//        Query query = em.createQuery(jpql).setParameter("no", no);
//        query.executeUpdate();
        Board board = em.find(Board.class, no);
        em.remove(board);
        em.flush();
    }

    @Override
    public int countAll(PageDto pageDto, BoardType boardType) {
        Query query = em.createQuery("select b from Board b where b.boardType = :category")
                .setParameter("category", boardType);
        return query.getResultList().size();
    }

    @Override
    public Board isPresentBoard(Long no) {
        return em.find(Board.class, no);
    }

    @Override
    public List<Board> searchByHint(PageDto pageDto, String hint, BoardType boardType) {
        List<Board> result = em.createQuery("select b from Board b where b.content like concat('%', :hint, '%') and b.boardType = :category order by b.no desc", Board.class)
                .setParameter("hint", hint)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<Board> searchByUser(PageDto pageDto, String user, BoardType boardType) {
        List<Board> result = em.createQuery("select b from Board b where b.member.id = (select m.id from Member m where m.userId = :user) and b.boardType = :category order by b.no desc", Board.class)
                .setParameter("user", user)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<Board> searchByTitle(PageDto pageDto, String title, BoardType boardType) {
        List<Board> result = em.createQuery("select b from Board b where b.title like concat('%', :title, '%') and b.boardType = :category", Board.class)
                .setParameter("title", title)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart())
                .setMaxResults(pageDto.getEnd())
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<Board> searchByRecommend(PageDto pageDto, BoardType boardType) {
        List<Board> result = em.createQuery("select b from Board b where b.boardType = :category order by b.recommend desc", Board.class)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart())
                .setMaxResults(pageDto.getEnd())
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<Board> findCategory(BoardType category) {
        return em.createQuery("select b from Board b where b.boardType = :category", Board.class)
                .setParameter("category", category)
                .getResultList();
    }

}
