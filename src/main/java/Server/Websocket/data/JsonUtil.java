package Server.Websocket.data;

import Server.Websocket.Server.Type;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;

public class JsonUtil {

    public static String formatMessage(int type,int select,ArrayList<Integer> table, String user) {
        JSONArray jsonArray = new JSONArray();
        JSONObject object = new JSONObject();
        jsonArray.addAll(table);
        object.put("type",type);
        object.put("select",select);
        object.put("sender",user);
        object.put("table",jsonArray);
        System.out.println(object.toJSONString());
        return object.toJSONString();
    }

    public static String formatDataSend(DataSend data){
        JSONObject send = new JSONObject();

        send.put("type",String.valueOf(data.type));
        send.put("dataTable",data.dataTable);
        send.put("select",String.valueOf(data.select));

        return send.toJSONString();
    }

    public static DataSend decodeDataSend(String json) throws ParseException {
        JSONObject in = (JSONObject) new JSONParser().parse(json);
        DataSend dataSend = new DataSend();
        dataSend.select = Integer.valueOf((String) in.get("select"));
        dataSend.type = Integer.valueOf((String) in.get("type"));
        dataSend.dataTable = (ArrayList<Integer>) in.get("dataTable");
        return dataSend;
    }

    public static void main(String[] args) throws ParseException {
        DataSend data = new DataSend();
        data.type = Type.CLIS.getValue();
        data.select = 1;
        data.dataTable = new ArrayList<>();
        data.dataTable.add(1);
        data.dataTable.add(1);
        data.dataTable.add(1);
        data.dataTable.add(1);
        String s = "{\"select\":\"1\",\"dataTable\":null,\"type\":\"0\"}";
        System.out.println(decodeDataSend(s).select);

    }
}