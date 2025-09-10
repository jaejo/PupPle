package jajo.jajo_ex.service;

import jajo.jajo_ex.DatabaseCleanUp;
import jajo.jajo_ex.domain.Member;
import jajo.jajo_ex.dto.JwtToken;
import jajo.jajo_ex.dto.MemberDto;
import jajo.jajo_ex.dto.SignInDto;
import jajo.jajo_ex.dto.SignUpDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MemberControllerTest {
    @Autowired
    DatabaseCleanUp databaseCleanUp;
    @Autowired
    MemberService memberService;
    @Autowired
    TestRestTemplate testRestTemplate;
    @LocalServerPort
    int randomServerPort;

    private SignUpDto signUpDto;

    @BeforeEach
    void beforeEach() {
        signUpDto = SignUpDto.builder()
                .username("lee")
                .userId("lee123")
                .password("0000")
                .build();
    }

    @AfterEach
    void afterEach() {
        databaseCleanUp.truncateAllEntity();
    }

    @Test
    public void signUpTest() {
        String url = "http://localhost:" + randomServerPort + "/member1/sign-up";
        ResponseEntity<MemberDto> responseEntity = testRestTemplate.postForEntity(url, signUpDto, MemberDto.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        MemberDto savedMemberDto = responseEntity.getBody();
        assertThat(savedMemberDto.getUsername()).isEqualTo(signUpDto.getUsername());
        assertThat(savedMemberDto.getUser_id()).isEqualTo(signUpDto.getUserId());
    }

    @Test
    public void signInTest() {
        memberService.signUp(signUpDto);

        SignInDto signInDto = SignInDto.builder()
                .userId("lee123")
                .password("0000").build();

        JwtToken jwtToken = memberService.signIn(signInDto);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwtToken.getAccessToken());
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        log.info("httpHeaders = {}", httpHeaders);

        String url = "http://localhost:" + randomServerPort + "/member1/test";
        ResponseEntity<String> responseEntity = testRestTemplate.postForEntity(url, new HttpEntity<>(httpHeaders), String.class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(signInDto.getUserId());
    }
}
