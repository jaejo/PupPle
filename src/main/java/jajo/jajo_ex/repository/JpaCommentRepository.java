package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;

public class JpaCommentRepository implements CommentRepository{

    private final EntityManager em;

    public JpaCommentRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Comment save(Comment comment) {
        em.persist(comment);
        return comment;
    }

    @Override
    @Transactional
    public Comment remove(Long id) {
        Comment comment = em.find(Comment.class, id);
        em.remove(comment);
        return comment;
    }

    @Override
    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    @Override
    public List<Comment> findBoardNo(Long no) {
        return em.createQuery("select c from Comment c where c.board.no = :no", Comment.class)
                .setParameter("no", no)
                .getResultList();
    }

    @Override
    public Comment isPresentComment(Long id) {
        return em.find(Comment.class, id);
    }

    @Override
    public List<Comment> findAllByBoard(Board board) {
        String query = "select c from Comment c LEFT JOIN FETCH c.parent l where c.board.no = :no order by c.parent.id asc nulls first, c.createdAt asc";
        return em.createQuery(query, Comment.class)
                .setParameter("no", board.getNo())
                .getResultList();
    }
//    @Override
//    public List<Comment> findAllByBoard(Board board) {
//        return jpaQueryFactory.selectFrom()
//                .leftJoin(comment.parent)
//                .fetchJoin()
//                .where(comment.board.no.eq(board.getNo()))
//                .orderBy(comment.board.no.asc().nullFirst(), comment.createAt.asc())
//                .fetch();
//    }
}
