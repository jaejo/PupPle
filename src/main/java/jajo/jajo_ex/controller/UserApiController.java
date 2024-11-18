package jajo.jajo_ex.controller;

import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApiController {
    private final MemberService memberService;

    @Autowired
    public UserApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping("/userInfo")
    public ResponseDto<Integer> update(@RequestBody Member member){
        memberService.update(member);
        return ResponseDto.success(1);
    }
}
