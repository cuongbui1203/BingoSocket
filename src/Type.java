public enum Type {
    HELLO(0), // goi tin chao hoi
    SERVER_SS(1), // server san sang de tao tro choi
    SS(2), // client san sang choi
    START(3), // game bat dau
    HIT(4), // client gui nuoc di
    REFUSE(5), // server tu choi nuoc di
    ACCEPT(6), // server chap nhan nuoc di
    GG(7), // ket thuc game. server gui ket qua tran dau.
    FF(8), // client tu bo game.
    REFUSE_2(9), // client tu choi ket noi va dong ket noi.
    CLIS(10); // server quyey dinh clinet nao dc danh

    private final int value;

    Type(int value) {
        this.value = value;
    }

    // public static Optional<Type> valueOf(int value) {
    // return Arrays.stream(values())
    // .filter(type -> type.value == value)
    // .findFirst();
    // }

    public int getValue() {
        return value;
    }
}