package jajo.jajo_ex.controller;

import jajo.jajo_ex.domain.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Controller
public class HomeController {
    @GetMapping("/")
    public String home(@SessionAttribute(required = false, name="principal") Member principal, Model model) {
        if (principal != null) model.addAttribute("member", principal);
        return "home";
    }
}
