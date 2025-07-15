package jajo.jajo_ex;

import jajo.jajo_ex.aop.TimeTraceAop;
import jajo.jajo_ex.repository.*;
import jajo.jajo_ex.service.*;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;


// 스프링 빈 등록

@Configuration
public class SpringConfig {

    private EntityManager em;

    public SpringConfig(EntityManager em) {
        this.em = em;
    }

    @Bean
    public MemberService memberService() {
        return new MemberService(memberRepository());
    }

    @Bean
    public BoardService boardService() {
        return new BoardService(boardRepository());
    }

    @Bean
    public BoardServiceV2 boardServiceV2() {return new BoardServiceV2(boardRepositoryV2());}
    @Bean
    public CommentService commentService() { return new CommentService(commentRepository());}

    @Bean
    public BoardFileService boardFileService() { return new BoardFileService(boardFileRepository());}
    //DeleteMapping 사용하기 위해
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }

    // @component와 같음
//    @Bean
//    public TimeTraceAop timeTraceAop() {
//        return new TimeTraceAop();
//    }
    @Bean
    public MemberRepository memberRepository() {
        return new JpaMemberRepository(em);
    }

    @Bean
    public BoardRepository boardRepository() {
        return new JpaBoardRepository(em);
    }

    @Bean
    public BoardRepositoryV2 boardRepositoryV2() {return new JpaBoardRepositoryV2(em);}

    @Bean
    public CommentRepository commentRepository() { return new JpaCommentRepository(em); }

    @Bean
    public BoardFileRepository boardFileRepository() { return new JpaBoardFileRepository(em); }
}
