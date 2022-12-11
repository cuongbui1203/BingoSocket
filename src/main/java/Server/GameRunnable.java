package Server;

import Logic.Player;
import Server.Websocket.data.MessageGame;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameRunnable extends Thread{
    Player p1,p2;
    Session sessionP1,sessionP2;

    MessageGame messageGame;
    ArrayList<AtomicBoolean> hit;
    public GameRunnable(Session p1,Session p2){
        sessionP1 = p1;
        this.p1 = new Player(sessionP1.getId());
        sessionP2 = p2;
        this.p2 = new Player(sessionP2.getId());
        hit.add(new AtomicBoolean(false));
        hit.add(new AtomicBoolean(false));
        messageGame = null;
    }

    public void setMessage(MessageGame messageGame) {
        this.messageGame = messageGame;
    }

    private void startGame() throws EncodeException, IOException {


    }
    @Override
    public void run() {

        hit.get(0).set(true);
        int idhit = 0,idnohit=1;
        while (!p1.CheckWinCon()&&!p2.CheckWinCon()){

        }
    }
}
