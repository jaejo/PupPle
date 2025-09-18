package jajo.jajo_ex;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;

public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            // 로그인 안 됐을 때
//            response.setContentType("application/json");
//            response.setCharacterEncoding("UTF-8");
//            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 상태코드도 가능
//            response.getWriter().write("{\"msg\":\"로그인이 필요한 서비스입니다.\", \"redirect\":\"/login\"}");
//            return;
//        }

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"msg\":\"로그아웃 완료\"}");
        response.setStatus(HttpServletResponse.SC_OK);

    }
}
