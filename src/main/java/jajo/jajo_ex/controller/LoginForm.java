package jajo.jajo_ex.controller;

import org.springframework.stereotype.Controller;

@Controller
public class LoginForm {
    private String userId;
    private String userPw;
    private String name;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPw() {
        return userPw;
    }

    public void setUserPw(String userPw) {
        this.userPw = userPw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
