package Server.Websocket.Server.Manager.Util;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;
public class MessageDecoder implements Decoder.Text<Server.Websocket.Server.Manager.Util.Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }



    @Override
    public Message decode(final String textMessage) throws DecodeException {

        Message message = new Message();
        JSONParser parser=new JSONParser();
        JSONObject jsonObject;
        try{
            jsonObject = (JSONObject) parser.parse(textMessage);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        message.setAction(Integer.parseInt(jsonObject.get("action").toString()));
        message.setId2(Integer.parseInt(jsonObject.get("id2").toString()));
        message.setId1(Integer.parseInt(jsonObject.get("id1").toString()));
        message.setMatch(Integer.parseInt(jsonObject.get("match").toString()));
        message.setPasswd(String.valueOf(jsonObject.get("passwd")));
//        message.setIp(Integer.(jsonObject.get("status")));
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