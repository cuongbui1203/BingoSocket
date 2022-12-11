package Server.Websocket.Server.Web.Util;

import org.json.simple.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Server.Websocket.Server.Web.Util.Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final Message message) throws EncodeException {
        String res = "";
        JSONObject object = new JSONObject();
        object.put("action",message.getAction());
        object.put("ip",message.getIp());
        object.put("port",message.getPort());
        object.put("path",message.getPath());
        object.put("result",message.getResult());
        object.put("id1",message.getId1());
        object.put("id2",message.getId2());
        object.put("status",message.getStatus());
        object.put("passwd", message.getPasswd());
        object.put("match",message.getMatch());
        return object.toJSONString();
    }

}