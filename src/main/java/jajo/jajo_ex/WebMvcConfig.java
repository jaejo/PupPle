package jajo.jajo_ex;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistrar;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns("/detail", "/newBoard", "/newBoardV2", "/updateBoard", "/updateForm",
                        "/createComment", "/updateComment", "/deleteComment",
                        "/myPage", "/detailV2")
                .excludePathPatterns("/", "/boards", "/login");
    }
    //정적 리소스 경로 설정(이미지를 불러올 때 서버를 재시작 안해도 됨)
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/**")
//                .addResourceLocations("file:src/main/resources/static/");

        registry.addResourceHandler("/uploadImage/**")
                .addResourceLocations("file:src/main/resources/static/uploadImage/");
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PostEnumConverter());
    }
}
