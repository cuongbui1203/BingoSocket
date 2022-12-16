package Server.Websocket.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;

public class MessageGameDecoder implements Decoder.Text<MessageGame> {

    @Override
    public void init(final EndpointConfig ctonfig) {
    }

    @Override
    public void destroy() {
    }



    @Override
    public MessageGame decode(final String textMessage) throws DecodeException {
        MessageGame messageGame = new MessageGame();
        JSONParser parser=new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(textMessage);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Integer> table = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get("table");
        for (Object o : jsonArray) {
            table.add(Integer.parseInt(o.toString()));
        }
        messageGame.setTable(table);
        messageGame.setType(Integer.parseInt(jsonObject.get("type").toString()));
        messageGame.setSender(String.valueOf(jsonObject.get("sender")));
        messageGame.setSelect(Integer.parseInt(jsonObject.get("select").toString()));
        messageGame.setPass(String.valueOf(jsonObject.get("pass")));
        return messageGame;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }

    public static void main(String[] args) throws DecodeException {
        MessageGame m = new MessageGameDecoder().decode("{\"type\":0,\"select\":1,\"sender\":\"wed\",\"table\":[1,2]}");
        System.out.println(m);
    }
}