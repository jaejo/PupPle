//package jajo.jajo_ex;
//
//import jajo.jajo_ex.domain.Member;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//public class LoginInterceptor implements HandlerInterceptor {
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
//                             Object handler) throws Exception {
//        Member principal = (Member) request.getSession().getAttribute("principal");
//        if (principal == null) {
////            response.sendRedirect("/?redirectURL=" + requestURI);
//            response.sendRedirect("/login");
//            return false;
//        } else {
//            return true;
//        }
//    }
//}
