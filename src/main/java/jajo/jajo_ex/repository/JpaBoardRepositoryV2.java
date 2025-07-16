package jajo.jajo_ex.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import jajo.jajo_ex.BoardType;
import jajo.jajo_ex.domain.BoardV2;
import jajo.jajo_ex.dto.PageDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

public class JpaBoardRepositoryV2 implements BoardRepositoryV2 {

    private final EntityManager em;

    public JpaBoardRepositoryV2(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public BoardV2 save(BoardV2 board) {
        try {
            //map형태에서 JSON로 변환
            board.update();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        em.persist(board);
        return board;
    }

    //좋아요 구현을 위해 db값을 변경하기 위해 em.persist 사용x
    @Override
    public void increaseRecommend(Long no) {
        em.createQuery("update BoardV2 b set b.recommend = b.recommend + 1 where b.no =:no")
                .setParameter("no", no)
                .executeUpdate();
    }

    @Override
    public List<BoardV2> findUserBoardAll(String userId) {
        List<BoardV2> result = em.createQuery("select b from Member m, BoardV2 b where m.userId = :userId", BoardV2.class)
                .setParameter("userId", userId)
                .getResultList();
        return result;
    }

    @Override
    public List<BoardV2> findAll() {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b", BoardV2.class)
                .getResultList();
        return result;
    }

    @Override
    public List<BoardV2> findBoardAll(PageDto pageDto, BoardType boardType) {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b where b.boardType = :category order by b.no desc", BoardV2.class)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        return result;
    }

    @Override
    public BoardV2 findByNo(Long no) {
        return em.find(BoardV2.class, no);
    }

    @Override
    public void delete(Long no) {
//        String jpql = "delete from Board b where b.no =:no";
//        Query query = em.createQuery(jpql).setParameter("no", no);
//        query.executeUpdate();
        BoardV2 board = em.find(BoardV2.class, no);
        em.remove(board);
        em.flush();
    }

    @Override
    public int countAll(PageDto pageDto, BoardType boardType) {
        Query query = em.createQuery("select b from BoardV2 b where b.boardType = :category")
                .setParameter("category", boardType);
        return query.getResultList().size();
    }

    @Override
    public BoardV2 isPresentBoard(Long no) {
        return em.find(BoardV2.class, no);
    }

    @Override
    public List<BoardV2> searchByHint(PageDto pageDto, String hint, BoardType boardType) {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b where b.content like concat('%', :hint, '%') and b.boardType = :category order by b.no desc", BoardV2.class)
                .setParameter("hint", hint)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<BoardV2> searchByUser(PageDto pageDto, String user, BoardType boardType) {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b where b.member.id = (select m.id from Member m where m.userId = :user) and b.boardType = :category order by b.no desc", BoardV2.class)
                .setParameter("user", user)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart()) // start부터
                .setMaxResults(pageDto.getEnd()) // end개수만큼
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<BoardV2> searchByTitle(PageDto pageDto, String title, BoardType boardType) {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b where b.title like concat('%', :title, '%') and b.boardType = :category", BoardV2.class)
                .setParameter("title", title)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart())
                .setMaxResults(pageDto.getEnd())
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<BoardV2> searchByRecommend(PageDto pageDto, BoardType boardType) {
        List<BoardV2> result = em.createQuery("select b from BoardV2 b where b.boardType = :category order by b.recommend desc", BoardV2.class)
                .setParameter("category", boardType)
                .setFirstResult(pageDto.getStart())
                .setMaxResults(pageDto.getEnd())
                .getResultList();
        pageDto.setTotalCount(result.size());
        return result;
    }

    @Override
    public List<BoardV2> findCategory(BoardType category) {
        return em.createQuery("select b from BoardV2 b where b.boardType = :category", BoardV2.class)
                .setParameter("category", category)
                .getResultList();
    }

}
