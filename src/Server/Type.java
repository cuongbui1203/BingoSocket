package Server;

public enum Type {
    /**
     * null
     */
    NULL(-1),
    /**
     * goi tin chao hoi
     */
    HELLO(0),
    /**
     * server san sang de tao tro choi
     */
    SERVER_SS(1),
    /**
     * client san sang choi
     */
    SS(2),
    /**
     * game bat dau
     */
    START(3),
    /**
     * client gui nuoc di
     */
    HIT(4),
    /**
     * server tu choi nuoc di
     */
    REFUSE(5),
    /**
     * server chap nhan nuoc di
     */
    ACCEPT(6),
    /**
     * ket thuc game. server gui ket qua tran dau.
     */
    GG(7),
    /**
     * client tu bo game.
     */
    FF(8),
    /**
     * client tu choi ket noi va dong ket noi.
     */
    REFUSE_2(9),
    /**
     * server quyet dinh client nao dc danh
     */
    CLIS(10),
    /**
     * send information of player table
     */
    TABLES(11),
    /**
     * send score to client.
     */
    SCORE(12),
    /**
     * send score to client.
     */
    HITTED(13);

    private final int value;

    Type(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}