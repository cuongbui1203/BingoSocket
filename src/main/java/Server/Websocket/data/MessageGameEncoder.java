package Server.Websocket.data;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageGameEncoder implements Encoder.Text<MessageGame> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final MessageGame messageGame) throws EncodeException {
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        jsonArray.addAll(messageGame.getTable());
        object.put("type",messageGame.getType());
        object.put("select",messageGame.getSelect());
        object.put("sender",messageGame.getSender());
        object.put("table",jsonArray);
        object.put("pass",messageGame.getPass());
//        System.out.println(object.toJSONString());
        return object.toJSONString();
    }

}