package jajo.jajo_ex.controller;

import jajo.jajo_ex.SecurityUtil;
import jajo.jajo_ex.dto.JwtToken;
import jajo.jajo_ex.dto.MemberDto;
import jajo.jajo_ex.dto.SignInDto;
import jajo.jajo_ex.dto.SignUpDto;
import jajo.jajo_ex.service.MemberService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/member1")
public class MemberControllerV2 {
    @PostConstruct
    public void init() {
        log.info("MemberControllerV2 bean 등록 완료");
    }

    private final MemberService memberService;

    @PostMapping("/sign-in")
    public JwtToken signIn(@RequestBody SignInDto signInDto) {
        String userId = signInDto.getUserId();
        String password = signInDto.getPassword();
        JwtToken jwtToken = memberService.signIn(userId, password);
        log.info("request username = {}, password = {}", userId, password);
        log.info("jwtToken accessToken = {}, refreshToken = {}", jwtToken.getAccessToken(), jwtToken.getRefreshToken());

        return jwtToken;
    }

    @PostMapping("/sign-up")
    public ResponseEntity<MemberDto> signUp(@RequestBody SignUpDto signUpDto) {
        MemberDto savedMemberDto = memberService.signUp(signUpDto);
        return ResponseEntity.ok(savedMemberDto);
    }

    @PostMapping("/test")
    public String test() {
        return SecurityUtil.getCurrentUsername();
    }
}
