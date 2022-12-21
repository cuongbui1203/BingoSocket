package Server.Websocket.Server.Manager.Util;

import lombok.Data;


/**
 * Nhận
 */
@Data
public class Message {
    private int action;
    private int result;
    private int match;
    private int id1;
    private int id2;
    private String passwd;
    private String ip;
    private int port;
    private String path;
    private String status;
}
