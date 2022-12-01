package Server.websocket.data;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;

@Data
public class Message {
    private int type;
    private int select;

    private ArrayList<Integer> table;

    private String sender;
}
