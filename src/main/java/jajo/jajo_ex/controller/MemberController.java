package jajo.jajo_ex.controller;

import jajo.jajo_ex.SessionConst;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    @GetMapping("/members/new")
    public String createForm() {
        return "members/createMemberForm";
    }
    @PostMapping("/members/new")
    public String create(MemberForm form) {
        Member member = new Member();

        member.setUserId(form.getUserId());
        member.setUserPw(form.getUserPw());
        member.setName(form.getName());

        memberService.join(member);

        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model, HttpServletRequest request) {

        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @PostMapping("/ajaxIdUrl")
    @ResponseBody
    public int idAjax(@RequestParam String userId){
        Member member = new Member();
        member.setUserId(userId);
        return memberService.validateDuplicateMember(member);
    }
    @PostMapping("/ajaxPwUrl")
    @ResponseBody
    public int pwAjax(@RequestParam String userPw, boolean pwFlag) {
        if(pwFlag){
            return 1;
        } else return 0;
    }

    @GetMapping("/myPage")
    public String mypage(
            @SessionAttribute(required = false, name="principal") Member principal, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        return "members/myPage";
    }
}
