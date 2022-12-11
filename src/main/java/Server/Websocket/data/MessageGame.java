package Server.Websocket.data;

import lombok.Data;

import java.util.ArrayList;

@Data
public class MessageGame {
    private int type;
    private int select;
    private String pass;
    private ArrayList<Integer> table;

    private String sender;
}
