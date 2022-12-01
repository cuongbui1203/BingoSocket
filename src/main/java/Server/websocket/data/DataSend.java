package Server.websocket.data;

import org.json.simple.JSONObject;

import java.util.ArrayList;

public class DataSend {
    public int type;

    public ArrayList<Integer> dataTable;

    public int select;

    public DataSend(){
        type = -1;
        dataTable = null;
        select = -1;
    }
}
