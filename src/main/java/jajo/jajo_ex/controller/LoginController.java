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
            ObjectMapper objectMapper = new ObjectMapper();
            String jwtTokenJson = objectMapper.writeValueAsString(jwtToken);
            String encodedValue = URLEncoder.encode(jwtTokenJson, StandardCharsets.UTF_8);
            Cookie cookie = new Cookie("jwtToken", encodedValue);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(60 * 60);
            httpResponse.addCookie(cookie);
        }
        response.put("jwtToken", jwtToken);
        response.put("prevPage", request.getSession().getAttribute("prevPage"));

        return response;
    }
//    @PostMapping("/login")
//    public String checkLogin(@SessionAttribute(required = false, name="principal") Member principal, @ModelAttribute LoginForm form, Model model, HttpServletRequest request) {
//        if (principal != null) return "redirect:/";
//
//        Member result = memberService.login(form.getUserId(), form.getUserPw());
//
//        if(result == null) {
//            model.addAttribute("msg", "아이디 비밀번호를 확인하세요.");
//            return "members/checkLogin";
//        }
////        Member member = Member.builder()
////                .id(result.getId())
////                .userId(form.getUserId())
////                .userPw(form.getUserPw())
////                .name(result.getName())
////                .build();
////        session.setAttribute("principal", member);
//
//        if(request.getSession().getAttribute("prevPage") == null) return "redirect:/";
//        else return "redirect:" + request.getSession().getAttribute("prevPage");
//    }

//    @PostMapping("/logout")
//    public String logout(HttpServletRequest request) {
//        HttpSession session = request.getSession(false);
//        if(session != null) {
//            session.invalidate();
//        }
//        return "redirect:/";
//    }
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        //서버에서 클라이언트에 저장된 JWT 쿠키를 만료시켜 삭제
        Cookie cookie = new Cookie("jwtToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/";
    }
}
