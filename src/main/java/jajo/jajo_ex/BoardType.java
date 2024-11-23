package jajo.jajo_ex;

public enum BoardType implements CodedEnum<Integer> {
    Hospital(100),
    place(200),
    product(300),
    free(400);

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
}
