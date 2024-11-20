package jajo.jajo_ex.dto;

import jajo.jajo_ex.domain.BoardFile;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FileRequestDto {
    private String fileName;

}
