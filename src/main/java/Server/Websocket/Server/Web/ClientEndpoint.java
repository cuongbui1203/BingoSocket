package Server.Websocket.Server.Web;

import Server.Websocket.data.MessageGame;
import Server.Websocket.data.MessageGameDecoder;
import Server.Websocket.data.MessageGameEncoder;

import javax.websocket.*;
import java.io.IOException;

import static java.lang.String.format;

@javax.websocket.ClientEndpoint(encoders = MessageGameEncoder.class, decoders = MessageGameDecoder.class)
public class ClientEndpoint extends Endpoint {


    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException {
        System.out.println(format("%s connect to sever.", session.getId()));
    }

    @OnMessage
    public void onMessage(MessageGame messageGame, Session session) throws IOException, EncodeException {
        System.out.println(messageGame);
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        System.out.println(format("%s connect to sever.", session.getId()));
    }
}
