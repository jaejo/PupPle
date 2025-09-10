package jajo.jajo_ex.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@EqualsAndHashCode(of="id")
public class Member implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name= "MEMBER_ID", updatable = false, unique = true, nullable = false)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    @Builder.Default
    @OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
    private List<Board> boards = new ArrayList<Board>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
    private List<BoardV2> boardV2s = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "member", cascade=CascadeType.REMOVE)
    private List<Comment> comments = new ArrayList<Comment>();

    public void addBoard(Board board){
        this.boards.add(board);
        board.setMember(this);
    }

    public void addBoardV2(BoardV2 boardv2) {
        this.boardV2s.add(boardv2);
        boardv2.setMember(this);
    }

    public void addComment(Comment comment) {
        this.comments.add(comment);
        comment.setMember(this);
    }
}
