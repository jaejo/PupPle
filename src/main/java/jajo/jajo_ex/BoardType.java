package jajo.jajo_ex;

public enum BoardType implements CodedEnum<Integer> {
    hospital(100),
    place(200),
    product(300),
    recommend(400),
    free(500);

    private final int code;

    BoardType(int code) {
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    @jakarta.persistence.Converter(autoApply = true)
    static class Converter extends AbstractCodedEnumConverter<BoardType, Integer> {
        public Converter() {
            super(BoardType.class);
        }
    }

    public static BoardType create(String requestCategory) {
        for (BoardType value : BoardType.values()) {
            if (value.toString().equals(requestCategory)) {
                return value;
            }
        }
        throw new IllegalStateException("일치하는 카테고리가 존재하지 않습니다.");
    }
}
