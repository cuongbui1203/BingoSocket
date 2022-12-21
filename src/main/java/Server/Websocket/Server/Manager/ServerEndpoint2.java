package Server.Websocket.Server.Manager;

import Server.Websocket.Server.ServerGame;
import Server.Websocket.Server.Manager.Util.Message;
import Server.Websocket.Server.Manager.Util.MessageDecoder;
import Server.Websocket.Server.Manager.Util.MessageEncoder;

import javax.websocket.*;
import java.io.IOException;
import java.net.InetAddress;
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
        System.out.println("server control: "+message);
//        System.out.println(session);
        if(message.getAction() == 1){
            if(gameThread == null){
                ServerGame server = new ServerGame(message.getPasswd(), message.getMatch(), String.valueOf(message.getId1()),String.valueOf(message.getId2()));
                gameThread = new Thread(server);
//                thread.start();
                Message send = new Message();
                send.setPort(server.getPort());
                send.setIp(InetAddress.getLocalHost().getHostAddress());
                send.setPath("game/bingo");
//                try {
//                    gameThread.start();
//                    send.setResult(1);
//                } catch (Exception e) {
//                    send.setResult(0);
//                    throw new RuntimeException(e);
//                }
                gameThread.start();
                send.setResult(1);
                session.getBasicRemote().sendObject(send);
            }
        }
//        if(message.getAction() == 1) {
//            gameThread = new Thread(new ServerGame(message.getPasswd(), message.getMatch(), String.valueOf(message.getId1()),String.valueOf(message.getId2())));
//            gameThread.start();
//        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {

    }

}