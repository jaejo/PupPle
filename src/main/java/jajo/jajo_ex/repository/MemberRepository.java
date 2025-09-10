package jajo.jajo_ex.repository;

import jajo.jajo_ex.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    Member save(Member member);
    Optional<Member> findById(Long id);
    Optional<Member> findByUsername(String username);

    Optional<Member> findByUserId(String userId);

    Optional<Member> checkLogin(String userId, String userPw);

    Optional<Member> findByName(String userId, String name);

    // Optional<Member> updatePw(String userId, String userPw);

    List<Member> findAll();

    Member findById(Member member);

    Member isPresentMember(Long id);

    Boolean existsByUsername(String username);
}
