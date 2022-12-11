package Server.Websocket.Server;

import Logic.Player;
import Server.Websocket.Server.Web.Util.Message;
import Server.Websocket.Server.Web.Util.MessageEncoder;
import Server.Websocket.data.MessageGame;
import Server.Websocket.data.MessageGameDecoder;
import Server.Websocket.data.MessageGameEncoder;


import javax.websocket.*;
import java.io.IOException;
import java.net.Socket;
import java.net.URISyntaxException;
import java.util.*;

import static java.lang.String.format;

@javax.websocket.server.ServerEndpoint(value = "/bingo", encoders = MessageGameEncoder.class, decoders = MessageGameDecoder.class)
public class ServerEndpoint {
    static boolean gameStart = false,gameEnd = false;
    static int hit = 0,noHit = 1;
    static boolean playerSend = false;
    static ArrayList<Player> players = new ArrayList<>();
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static String password = "";
    static int PORT = -1;
    static Socket socket;
    static int math = -1;
    static String p1Name;
    static String p2Name;

//    static int
    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException {
        System.out.printf("%s connect to sever Game.%n", session.getId());
    }

    @OnMessage
    public void onMessage(MessageGame messageGame, Session session) throws IOException, EncodeException {
        String user = (String) session.getUserProperties().get("user");
        System.out.println(messageGame);
        MessageGame messageGame1 = new MessageGame();
        messageGame1.setSender("Sever");
        messageGame1.setTable(new ArrayList<>());
        if (user == null) {
            session.getUserProperties().put("user", messageGame.getSender());
        }
        // thêm người chơi mới vô game nếu điền đúng tên và mật khẩu
        if (messageGame.getType() == Type.HELLO.getValue()) {
            if (
                    messageGame.getPass().equals(password) &&
                            (messageGame.getSender().equals(p1Name) || messageGame.getSender().equals(p2Name))
            ) {
                System.out.printf("%s join to game.%n", messageGame.getSender());
                peers.add(session);
                players.add(new Player(messageGame.getSender()));
            } else {
                messageGame1.setType(Type.REFUSE_2.getValue());
                session.getBasicRemote().sendObject(messageGame1);
                session.close();
            }
        }


        // khởi tạo game mới
        if (peers.size() == 2 && !gameStart) {
            gameStart = true;
            playerSend = false;
            System.out.println("gameStart");
            messageGame1.setType(Type.START.getValue());
            sendToPySever(Type.START, players.get(0), players.get(1));
            for (Session peer : peers) {
                //send start
                System.out.println(peer.getId());
                messageGame1.setType(Type.START.getValue());
                messageGame1.setSelect(0);
                messageGame1.setTable(new ArrayList<>());
                peer.getBasicRemote().sendObject(messageGame1);
                if (players.get(hit).getName().equals(user)) {
                    //send player table
                    messageGame1.setType(Type.TABLES.getValue());
                    messageGame1.setTable(players.get(hit).getPlayerTable());
                    peer.getBasicRemote().sendObject(messageGame1);
                    // gửi yêu cầu đánh cho người chơi
                    messageGame1.setType(Type.CLIS.getValue());
                    messageGame1.setSelect(1);
                    peer.getBasicRemote().sendObject(messageGame1);
                } else {
//                    gửi thông tin bàn chơi
                    messageGame1.setType(Type.TABLES.getValue());
                    messageGame1.setTable(players.get(noHit).getPlayerTable());
                    peer.getBasicRemote().sendObject(messageGame1);
                    // Gửi yêu cầu không đánh cho người chơi
                    messageGame1.setType(Type.CLIS.getValue());
                    messageGame1.setSelect(0);
                    peer.getBasicRemote().sendObject(messageGame1);
                }
            }
        }
        // xử lý khi game chạy
        if (gameStart) {

            System.out.println("hit");
            messageGame1.setSender("Sever");
            messageGame1.setTable(new ArrayList<>());

            int phit = messageGame.getSelect();
            System.out.println("hit: " + phit);
            players.get(hit).move(phit);
            players.get(noHit).move(phit);
            for (Session peer : peers) {
                assert user != null;
                if (user.equals(players.get(noHit).getName())) {
                    messageGame1.setType(Type.HIT.getValue());
                } else {
                    messageGame1.setType(Type.ACCEPT.getValue());
                }
                messageGame1.setSelect(phit);
                peer.getBasicRemote().sendObject(messageGame1);
            }
            int tg = hit;
            hit = noHit;
            noHit = tg;

            if (players.get(0).CheckWinCon() || players.get(1).CheckWinCon()) {
                gameStart = false;
                gameEnd = true;
            }
        }

        if (gameEnd) {
            sendToPySever(Type.GG, players.get(0), players.get(1));
            MessageGame messageGame2 = new MessageGame();
            messageGame2.setTable(new ArrayList<>());
            messageGame1.setType(Type.GG.getValue());
            messageGame1.setSelect(1); // thang
            messageGame2.setType(Type.GG.getValue());
            messageGame2.setSelect(0); // thua
            for (Session peer : peers) {
                if (players.get(hit).CheckWinCon()) {
                    assert user != null;
                    if (user.equals(players.get(hit).getName())) {
                        peer.getBasicRemote().sendObject(messageGame1);
                        messageGame1.setType(Type.SCORE.getValue());
                        messageGame1.setSelect(players.get(hit).getScore());
                        peer.getBasicRemote().sendObject(messageGame1);
                    } else {
                        peer.getBasicRemote().sendObject(messageGame2);
                        messageGame2.setType(Type.SCORE.getValue());
                        messageGame2.setSelect(players.get(noHit).getScore());
                        peer.getBasicRemote().sendObject(messageGame2);
                    }
                } else {
                    assert user != null;
                    if (user.equals(players.get(noHit).getName())) {
                        peer.getBasicRemote().sendObject(messageGame1);
                        messageGame1.setType(Type.SCORE.getValue());
                        messageGame1.setSelect(players.get(noHit).getScore());
                        peer.getBasicRemote().sendObject(messageGame1);
                    } else {
                        peer.getBasicRemote().sendObject(messageGame2);
                        messageGame2.setType(Type.SCORE.getValue());
                        messageGame2.setSelect(players.get(hit).getScore());
                        peer.getBasicRemote().sendObject(messageGame2);
                    }
                }
            }

        } else {
            sendToPySever(Type.SCORE, players.get(0), players.get(1));
        }
    }


    @OnClose
    public void onClose(Session session) throws IOException, EncodeException, URISyntaxException, DeploymentException {
        //notify peers about leaving the chat room
        System.out.printf("%s left game.%n", session.getId());
        sendToPySever(Type.GG,players.get(0),players.get(1));
        players.removeIf(player -> player.getName().equals(session.getId()));
        peers.remove(session);
        for (Session peer : peers) {
            MessageGame messageGame = new MessageGame();
            messageGame.setType(Type.GG.getValue());
            messageGame.setTable(new ArrayList<>());
            messageGame.setSelect(1);
            peer.getBasicRemote().sendObject(messageGame);
            messageGame.setType(Type.SCORE.getValue());
            messageGame.setSelect(players.get(0).getScore());
            peer.getBasicRemote().sendObject(messageGame);
        }



        System.exit(1);
    }


    private void sendToPySever(Type type,Player p1,Player p2) throws EncodeException, IOException {
//        if(pySever == null) return;\
        Message message = new Message();
        message.setResult(type.getValue());
        if(p1.getName().equals(p1Name)) {
            message.setId1(p1.getScore());
            message.setId2(p2.getScore());
        }else {
            message.setId1(p2.getScore());
            message.setId2(p1.getScore());
        }
        message.setMatch(math);
        String res = new MessageEncoder().encode(message);

        System.out.println(res);
        socket.getOutputStream().write(res.getBytes());
        socket.getOutputStream().flush();
//        pySever.getBasicRemote().sendObject(message);

    }
}
