package jajo.jajo_ex.service;

import jajo.jajo_ex.domain.Board;
import jajo.jajo_ex.domain.Comment;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.CommentResponseDto;
import jajo.jajo_ex.dto.PageDto;
import jajo.jajo_ex.dto.ResponseDto;
import jajo.jajo_ex.repository.BoardRepository;
import jajo.jajo_ex.repository.CommentRepository;
import jajo.jajo_ex.repository.JpaMemberRepository;
import jajo.jajo_ex.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired BoardRepository boardRepository;
    @Autowired BoardService boardService;
    @Autowired CommentService commentService;
    @Autowired CommentRepository commentRepository;

    @Test
    void 회원가입() {
        //given
        Member member = new Member();
        member.setUserId("spring1");
        member.setUserPw("1111");
        member.setName("jj");

        //when
        String saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getUserId()).isEqualTo(findMember.getUserId());
    }

    @Test
    public void 중복_회원_예외() {
        Member member1 = new Member();
        member1.setUserId("spring");

        Member member2 = new Member();
        member2.setUserId("spring");

        memberService.join(member1);
        assertThrows(IllegalStateException.class, () -> memberService.join(member2));

/*        try {
            memberService.join(member2);
            fail();
        } catch (IllegalStateException e) {
            assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");
        }*/

    }
    @Test
    public void 로그인() {
        Member member = new Member();
        String userId = "lee1234";
        String userPw = "!wowns159";

        memberService.login(userId, userPw);
    }
    @Test
    public void 조회() {
        Board b1 = new Board();
        b1.setTitle("title1");
        b1.setContent("안녕하세요.");
        boardRepository.save(b1);

        Board b2 = new Board();
        b2.setTitle("title2");
        b2.setContent("안녕히가세요.");
        boardRepository.save(b2);

        Member member1 = new Member();
        member1.setUserId("ljjn0520");
        member1.setName("이재준");
        member1.addBoard(b1);
        member1.addBoard(b2);
        memberRepository.save(member1);

        Optional<Member> findMember = memberRepository.findByUserId(member1.getUserId());
        List<Board> findUserBoard = boardRepository.findUserBoardAll(member1.getUserId());

        for(int i=0; i<2; i++) {
            System.out.println(findUserBoard.get(i).getContent());
        }

    }
//    @Test
//    public void 게시글() {
//        PageDto pageDto = new PageDto();
//        System.out.println(boardRepository.findBoardAll(pageDto, hint).subList(1, 10));
//    }

    @Test
    public void 댓글_작성() {
        Member member1 = new Member();
        member1.setUserId("lee123");
        member1.setUserPw("!wowns159357");

        Member member2 = new Member();
        member2.setUserId("kim123");
        member2.setUserPw("!wowns159357");

        memberRepository.save(member1);
        memberRepository.save(member2);

        Board board1 = new Board();
        board1.setTitle("안녕하세요.");
        board1.setContent("반갑습니다.");

        Board board2 = new Board();
        board2.setTitle("안녕하가세요.");
        board2.setContent("내일 봬요.");

        member1.addBoard(board1);
        member1.addBoard(board2);

        boardService.save(board1);
        boardService.save(board2);

        Comment comment1 = new Comment();
        comment1.setContent("저도 반가워요.");

        member2.addComment(comment1);
        board1.addComment(comment1);

        commentService.save(comment1);

        System.out.println(commentService.findComments());

        for(int i=0; i<1; i++) {
            System.out.println(commentService.findComments().get(i));
        }
    }
    @Test
    public void 대댓글() {
        Long no = 2L;
        Board board = boardService.isPresentBoard(no);
        List<Comment> commentList = commentService.findAllByBoard(board);

        List<CommentResponseDto> commentResponseDtoList = new ArrayList<>();
        Map<Long, CommentResponseDto> map = new HashMap<>();

        commentList.stream().forEach(c -> {
            CommentResponseDto cdto = new CommentResponseDto(c);

            if(c.getParent() != null) {
                cdto.setParentId(c.getParent().getId());
            }
            map.put(cdto.getId(), cdto);
            if(c.getParent() != null) map.get(c.getParent().getId()).getChildren().add(cdto);
            else commentResponseDtoList.add(cdto);

        });
    }
//    @Test
//    public void 질의() {
//
//        Board board1 = new Board();
//        board1.setContent("안녕하시오");
//        boardService.save(board1);
//
//        List<Board> result = boardService.searchByContent("안녕");
//        System.out.println(result.size());
//
//    }
}
