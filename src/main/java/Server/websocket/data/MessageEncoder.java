package Server.websocket.data;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {

    @Override
    public void init(final EndpointConfig config) {
    }

    @Override
    public void destroy() {
    }

    @Override
    public String encode(final Message message) throws EncodeException {
        return JsonUtil.formatMessage(message.getType(),message.getSelect(),message.getTable(),message.getSender());
    }

}