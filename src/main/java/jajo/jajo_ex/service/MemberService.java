package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.repository.MemberRepository;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public class MemberService {
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getUserId();
    }

    public int validateDuplicateMember(Member member) {
        Optional<Member> userId = memberRepository.findByUserId(member.getUserId());
        if (userId.isPresent()) {
            return 0;
        } else {
            return 1;
        }
    }

    public Member login(String userId, String userPw) {
        Optional<Member> userInfo = memberRepository.checkLogin(userId, userPw);
        return userInfo.orElse(null);
    }
    
//    public void validateDuplicateMember(Member member) {
//        memberRepository.findByUserId(member.getUserId())
//            .ifPresent(m -> {
//                throw new IllegalStateException("이미 존재하는 회원입니다.");
//            });
//    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String memberUserId) {
        return memberRepository.findByUserId(memberUserId);
    }
    @Transactional
    public void update(Member member) {
        Optional<Member> updateUser = memberRepository.findByUserId(member.getUserId());
        updateUser.get().setUserId(member.getUserId());
        updateUser.get().setUserPw(member.getUserPw());
        updateUser.get().setName(member.getName());
    }

    public Member findById(Member member) {
        return memberRepository.findById(member);
    }

    public Member isPresentMember(Long id) {
        return memberRepository.isPresentMember(id);
    }

}
