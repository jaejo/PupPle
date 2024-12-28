package jajo.jajo_ex.controller;

import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

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
    public String checkLogin(@SessionAttribute(required = false, name="principal") Member principal, @ModelAttribute LoginForm form, Model model, HttpServletRequest request) {
        if (principal != null) return "redirect:/";

        Member result = memberService.login(form.getUserId(), form.getUserPw());

        if(result == null) {
            model.addAttribute("msg", "아이디 비밀번호를 확인하세요.");
            return "members/checkLogin";
        }
        Member member = Member.builder()
                .id(result.getId())
                .userId(form.getUserId())
                .userPw(form.getUserPw())
                .name(result.getName())
                .build();
        session.setAttribute("principal", member);

        if(request.getSession().getAttribute("prevPage") == null) return "redirect:/";
        else return "redirect:" + request.getSession().getAttribute("prevPage");
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if(session != null) {
            session.invalidate();
        }
        return "redirect:/";
    }
}
