package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.Member;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class JpaMemberRepository implements MemberRepository {

    private final EntityManager em;

    public JpaMemberRepository(EntityManager em) {
        this.em = em;
    }

    @Override
    @Transactional
    public Member save(Member member) {
        em.persist(member);
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        Member member = em.find(Member.class, id);
        return Optional.ofNullable(member);
    }

    @Override
    public Optional<Member> findByUserId(String userId) {
        List<Member> result = em.createQuery("select m from Member m where m.userId = :userId", Member.class)
                .setParameter("userId", userId)
                .getResultList();
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> checkLogin(String userId, String userPw) {
        List<Member> result = em.createQuery("select m from Member m where m.userId = :userId and m.userPw = :userPw", Member.class)
                .setParameter("userId", userId)
                .setParameter("userPw", userPw)
                .getResultList();
        return result.stream().findAny();
    }
    // 이름 중복이 있기 때문에 아이디와 이름 같이 조회
    @Override
    public Optional<Member> findByName(String userId, String name) {
        Member result = em.createQuery("select m.name from Member m where m.userId = :userId and m.name = :name", Member.class)
                .setParameter("userId", userId)
                .setParameter("name", name)
                .getSingleResult();
        return Optional.of(result);
    }
//    @Override
//    public Optional<Member> findByName(String name) {
//        List<Member> result = em.createQuery("select m from Member m where m.name= :name", Member.class)
//                .setParameter("name", name)
//                .getResultList();
//        return result.stream().findAny();
//    }

    @Override
    public List<Member> findAll() {
        List<Member> result = em.createQuery("select m from Member m ", Member.class)
                .getResultList();
        return result;
    }

    @Override
    public Member findById(Member member) {
        Member result = em.find(Member.class, member.getId());
        return result;
    }

    @Override
    public Member isPresentMember(Long id) {
        return em.find(Member.class, id);
    }


}
