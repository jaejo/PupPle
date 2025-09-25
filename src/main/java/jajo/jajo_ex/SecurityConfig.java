package jajo.jajo_ex;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // REST API이므로 basic auth 및 csrf 보안을 사용하지 않음
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                // JWT를 사용하기 때문에 세션을 사용하지 않음
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // 해당 API에 대해서는 모든 요청을 허가
                        .requestMatchers("/", "/header.html", "/css/**", "/js/**", "/images/**", "/uploadImage/**").permitAll()
                        // -게시판 API-
                        .requestMatchers("/boards").permitAll()

                        //로그인, (아이디 중복, 비밀번호 정규식 확인 => ajax)
                        .requestMatchers("/login", "/validateDuplicateMemberId", "/ajaxPwUrl").permitAll()
                        .requestMatchers("/newMember").permitAll()

                        //로그인 여부 확인 API
                        .requestMatchers("/auth/**").permitAll()

                        .requestMatchers("/newBoardV2", "/detailV2", "/recommend", "/deleteForm", "/updateBoardV2", "/createComment").hasRole("USER")


                        // 이 밖에 모든 요청에 대해서 인증을 필요로 한다는 설정
                        .anyRequest().authenticated()
                )
//                .exceptionHandling(exception -> exception
//                        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
//                )
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler(new CustomLogoutSuccessHandler())
                        .invalidateHttpSession(false)
                        .permitAll()
                )
                // JWT 인증을 위하여 직접 구현한 필터를 UsernamePasswordAuthenticationFilter 전에 실행
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt Encoder 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

}