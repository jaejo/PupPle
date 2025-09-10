package jajo.jajo_ex.service;

import jajo.jajo_ex.CustomUserDetailsService;
import jajo.jajo_ex.JwtTokenProvider;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.JwtToken;
import jajo.jajo_ex.dto.MemberDto;
import jajo.jajo_ex.dto.SignInDto;
import jajo.jajo_ex.dto.SignUpDto;
import jajo.jajo_ex.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

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

    public int validateDuplicateMember(String userId) {
        Optional<Member> checkUserId = memberRepository.findByUserId(userId);

        if(checkUserId.isPresent()) return 0;
        else return 1;
    }



    @Transactional
    public JwtToken signIn(String userId, String password) {
        // 1. username + password 를 기반으로 Authentication 객체 생성
        // 이때 authentication 은 인증 여부를 확인하는 authenticated 값이 false
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userId, password);
        System.out.println("authenticationToken: " + authenticationToken );
        // 2. 실제 검증. authenticate() 메서드를 통해 요청된 Member 에 대한 검증 진행
        // authenticate 메서드가 실행될 때 CustomUserDetailsService 에서 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        // 3. 인증 정보를 기반으로 JWT 토큰 생성
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public JwtToken signIn(SignInDto signInDto) {
        // 1. AuthenticationToken 객체 생성 - 평문 비밀번호 사용
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(signInDto.getUserId(), signInDto.getPassword());
        try {
            // 2. AuthenticationManager에게 인증 요청 (UserDetailsService와 PasswordEncoder에 의해 검증됨)
            Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
            // 3. 인증 성공 시 SecurityContext에 인증 정보 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            // 4. 인증된 Authentication을 토대로 JWT 토큰 생성 및 반환
            return jwtTokenProvider.generateToken(authentication);
        } catch(AuthenticationException exception) {
            return null;
        }
    }

    @Transactional
    public MemberDto signUp(SignUpDto signUpDto) {
//        if(memberRepository.existsByUsername(signUpDto.getUsername())) {
//            throw new IllegalArgumentException("이미 사용 중인 사용자 아이디입니다.");
//        }
        String encodedPassword = passwordEncoder.encode(signUpDto.getPassword());
        List<String> roles = new ArrayList<>();
        roles.add("USER");
        return MemberDto.toDto(memberRepository.save(signUpDto.toEntity(encodedPassword, roles)));
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
//        updateUser.get().setUserId(member.getUserId());
//        updateUser.get().setUserPw(member.getUserPw());
//        updateUser.get().setName(member.getName());
    }

    public Member findById(Member member) {
        return memberRepository.findById(member);
    }

    public Member isPresentMember(Long id) {
        return memberRepository.isPresentMember(id);
    }

}
