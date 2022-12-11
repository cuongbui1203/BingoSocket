package Server.Websocket.Server.Web.Util;

import org.json.simple.JSONObject;

public class JsonUtil {

    public static String formatMessage2(int resul,String ip,int port,String path) {
        String res = "";
        JSONObject object = new JSONObject();
        object.put("result",resul);
        object.put("ip",ip);
        object.put("port",port);
        object.put("path",path);
        return object.toJSONString();
    }
    public static String formatMessage(int action,int match, int id1,int id2,String passwd) {
        JSONObject object = new JSONObject();
        object.put("action",action);
        object.put("match",match);
        object.put("id1",id1);
        object.put("id2",id2);
        object.put("passwd",passwd);
        return object.toJSONString();
    }

}