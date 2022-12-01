package Server.websocket.server;

import Logic.Player;
import Server.websocket.data.Message;
import Server.websocket.data.MessageDecoder;
import Server.websocket.data.MessageEncoder;


import javax.websocket.*;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

@javax.websocket.server.ServerEndpoint(value = "/chat", encoders = MessageEncoder.class, decoders = MessageDecoder.class)
public class ServerEndpoint {
    static boolean gameStart = false,gameEnd = false;
    static int hit = 0,noHit = 1;
    static boolean playerSend = false;
    static ArrayList<Player> players = new ArrayList<>();
    static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());

    @OnOpen
    public void onOpen(Session session) throws EncodeException, IOException {
        System.out.println(format("%s connect to sever.", session.getId()));
    }

    @OnMessage
    public void onMessage(Message message, Session session) throws IOException, EncodeException {
        String user = (String) session.getUserProperties().get("user");
        System.out.println(message);
        Message message1 = new Message();
        message1.setSender("Sever");
        message1.setTable( new ArrayList<>());
        if (user == null) {
            session.getUserProperties().put("user", message.getSender());
        }

        if(message.getType() == Type.HELLO.getValue()){
            System.out.println(format("%s join to game.", message.getSender()));
            peers.add(session);
            players.add(new Player(session.getId()));
        }

        if(peers.size() == 2 && !gameStart) {
            gameStart = true;
            playerSend = false;
            System.out.println("gameStart");
            for (Session peer:peers){
                //send start
                System.out.println(peer.getId());
                message1.setType(Type.START.getValue());
                message1.setSelect(0);
                message1.setTable(new ArrayList<>());
                peer.getBasicRemote().sendObject(message1);
                //send player table
                if(players.get(0).getName().equals(peer.getId())){
                    message1.setType(Type.TABLES.getValue());
                    message1.setTable(players.get(0).getPlayerTable());
                    peer.getBasicRemote().sendObject(message1);
                }else{
                    message1.setType(Type.TABLES.getValue());
                    message1.setTable(players.get(1).getPlayerTable());
                    peer.getBasicRemote().sendObject(message1);
                }
            }
        }

        if(gameStart){
            System.out.println("hit");
            message1.setSender("Sever");
            message1.setTable( new ArrayList<>());
            if(!playerSend) {
                for (Session peer : peers) {
                    if (peer.getId().equals(players.get(hit).getName())) {
                        message1.setType(Type.CLIS.getValue());
                        message1.setSelect(1);
                        peer.getBasicRemote().sendObject(message1);
                    } else {
                        message1.setType(Type.CLIS.getValue());
                        message1.setSelect(0);
                        peer.getBasicRemote().sendObject(message1);
                    }
                }
                playerSend = true;

            }else{
                int phit = message.getSelect();
                System.out.println("hit: "+phit);
                playerSend = false;
                players.get(hit).move(phit);
                players.get(noHit).move(phit);
                for (Session peer:peers){
                    if(peer.getId().equals(players.get(noHit).getName())){
                        message1.setType(Type.HIT.getValue());
                    }else {
                        message1.setType(Type.ACCEPT.getValue());
                    }
                    message1.setSelect(phit);
                    peer.getBasicRemote().sendObject(message1);
                }
                int tg = hit;
                hit = noHit;
                noHit = tg;
            }
            if(players.get(0).CheckWinCon()||players.get(1).CheckWinCon()){
                gameStart = false;
                gameEnd = true;
            }
        }

        if(gameEnd){
            Message message2 = new Message();
            message2.setTable(new ArrayList<>());
            message1.setType(Type.GG.getValue());
            message1.setSelect(1); // thang
            message2.setType(Type.GG.getValue());
            message2.setSelect(0); // thua
            for(Session peer:peers) {
                if (players.get(hit).CheckWinCon()) {
                    if(peer.getId().equals(players.get(hit).getName())) {
                        peer.getBasicRemote().sendObject(message1);
                        message1.setType(Type.SCORE.getValue());
                        message1.setSelect(players.get(hit).getScore());
                        peer.getBasicRemote().sendObject(message1);
                    }
                    else {
                        peer.getBasicRemote().sendObject(message2);
                        message2.setType(Type.SCORE.getValue());
                        message2.setSelect(players.get(noHit).getScore());
                        peer.getBasicRemote().sendObject(message2);
                    }
                }else{
                    if(peer.getId().equals(players.get(noHit).getName())) {
                        peer.getBasicRemote().sendObject(message1);
                        message1.setType(Type.SCORE.getValue());
                        message1.setSelect(players.get(noHit).getScore());
                        peer.getBasicRemote().sendObject(message1);
                    }
                    else {
                        peer.getBasicRemote().sendObject(message2);
                        message2.setType(Type.SCORE.getValue());
                        message2.setSelect(players.get(hit).getScore());
                        peer.getBasicRemote().sendObject(message2);
                    }
                }
            }
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException, EncodeException {
        System.out.println(format("%s left game.", session.getId()));
        peers.remove(session);
        players.removeIf(player -> player.getName().equals(session.getId()));
        //notify peers about leaving the chat room
        for (Session peer : peers) {
            Message message = new Message();
            message.setType(Type.GG.getValue());
            message.setTable(new ArrayList<>());
            message.setSelect(1);
            peer.getBasicRemote().sendObject(message);
            message.setType(Type.SCORE.getValue());
            message.setSelect(players.get(0).getScore());
            peer.getBasicRemote().sendObject(message);
        }
        System.exit(1);
    }

}