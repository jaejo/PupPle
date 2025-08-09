package jajo.jajo_ex.domain;

import jajo.jajo_ex.dto.BoardRequestDto;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class BoardFile {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String thumbnail;

    private String fileName;

//    private int addThumbnail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_NO")
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD2_NO")
    private BoardV2 boardV2;
    
}
