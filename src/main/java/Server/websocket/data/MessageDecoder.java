package Server.websocket.data;

import lombok.SneakyThrows;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
import java.util.ArrayList;

public class MessageDecoder implements Decoder.Text<Message> {

    @Override
    public void init(final EndpointConfig ctonfig) {
    }

    @Override
    public void destroy() {
    }



    @Override
    public Message decode(final String textMessage) throws DecodeException {
        Message message = new Message();
        JSONParser parser=new JSONParser();
        JSONObject jsonObject = null;
        try {
            jsonObject = (JSONObject) parser.parse(textMessage);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ArrayList<Integer> table = new ArrayList<>();
        JSONArray jsonArray = (JSONArray) jsonObject.get("table");
        for(int i = 0;i<jsonArray.size();i++){
            table.add(Integer.parseInt(jsonArray.get(i).toString()));
        }
        message.setTable(table);
        message.setType(Integer.parseInt(jsonObject.get("type").toString()));
        message.setSender(String.valueOf(jsonObject.get("sender")));
        message.setSelect(Integer.parseInt(jsonObject.get("select").toString()));

        return message;
    }

    @Override
    public boolean willDecode(final String s) {
        return true;
    }

    public static void main(String[] args) throws DecodeException {
        Message m = new MessageDecoder().decode("{\"type\":0,\"select\":1,\"sender\":\"wed\",\"table\":[1,2]}");
        System.out.println(m);
    }
}