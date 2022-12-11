package Server.Websocket.Server.Web;

import Server.Websocket.Server.Server;
import Server.Websocket.Server.Web.Util.Message;
import Server.Websocket.Server.Web.Util.MessageDecoder;
import Server.Websocket.Server.Web.Util.MessageEncoder;

import javax.websocket.*;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;


@javax.websocket.server.ServerEndpoint(value = "/", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint2 {
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static Thread gameThread = null;
    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException {
        System.out.printf("%s connect to sever container.%n", session.getId());
        peers.add(session);
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException, InterruptedException {
        System.out.println("server control"+message);
//        System.out.println(session);
//        if(message.getAction() == 1){
//            if(thread == null){
//                Server server = new Server(message.getPasswd());
//                thread = new Thread(server);
////                thread.start();
//                Message send = new Message();
//                send.setPort(server.getPort());
//                send.setIp("127.0.0.1");
//                send.setPath("/game/bingo");
//                try {
//                    thread.start();
//                    send.setResult(1);
//                } catch (Exception e) {
//                    send.setResult(0);
//                    throw new RuntimeException(e);
//                }
////                session.getBasicRemote().sendObject(send);
//            }
//        }
        if(message.getAction() == 1) {
            gameThread = new Thread(new Server(message.getPasswd(), message.getMatch(), String.valueOf(message.getId1()),String.valueOf(message.getId2())));
            gameThread.start();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

    }

}