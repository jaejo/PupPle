package jajo.jajo_ex;

import org.springframework.core.convert.converter.Converter;

public class PostEnumConverter implements Converter<String, BoardType> {
    @Override
    public BoardType convert(String requestCategory) {
        return BoardType.create(requestCategory.toUpperCase());
    }
}
