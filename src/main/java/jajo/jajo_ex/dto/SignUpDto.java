package jajo.jajo_ex.dto;

import jajo.jajo_ex.domain.Member;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {
    private String username;
    private String password;
    private String userId;
    private List<String> roles = new ArrayList<>();

    public Member toEntity(String encodedPassword, List<String> roles) {
        return Member.builder()
                .username(username)
                .password(encodedPassword)
                .userId(userId)
                .roles(roles)
                .build();
    }
}
