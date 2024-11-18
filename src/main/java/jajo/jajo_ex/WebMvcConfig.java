package jajo.jajo_ex;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/detail", "/newBoard", "/updateBoard", "/updateForm",
                        "/createComment", "/updateComment", "/deleteComment",
                        "/myPage")
                .excludePathPatterns("/", "/boards", "/login");
    }
}
