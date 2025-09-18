package jajo.jajo_ex.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.JwtToken;
import jajo.jajo_ex.dto.SignInDto;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final MemberService memberService;
    private final HttpSession session;

    @GetMapping("/login")
    public String loginForm(HttpServletRequest request) {
        String uri = request.getHeader("Referer");
        //login 페이지에서 로그인할 때 로그인으로 감
        request.getSession().setAttribute("prevPage", uri);
        return "members/checkLogin";
    }
    @PostMapping("/login")
    @ResponseBody
    public Map<String, Object> checkLogin(@RequestBody SignInDto signInDto, HttpServletRequest request, HttpServletResponse httpResponse) throws JsonProcessingException, UnsupportedEncodingException {
        JwtToken jwtToken = memberService.signIn(signInDto);

        Map<String, Object> response = new HashMap<>();
        if(jwtToken == null) response.put("msg", "아이디와 비밀번호를 확인하세요");
        else {
            //로그인 시 HTTP Only 쿠키에 넣어서 accessToken 관리
            //cookie 값에는 무조건 string -> 변환함
//            ObjectMapper objectMapper = new ObjectMapper();
//            String jwtTokenJson = objectMapper.writeValueAsString(jwtToken);
//            String encodedValue = URLEncoder.encode(jwtTokenJson, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie("jwt", jwtToken.getAccessToken());
            cookie.setHttpOnly(true);
            cookie.setSecure(false);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 30);
            httpResponse.addCookie(cookie);
        }
        response.put("jwtToken", jwtToken);
        response.put("prevPage", request.getSession().getAttribute("prevPage"));

        return response;
    }
}
