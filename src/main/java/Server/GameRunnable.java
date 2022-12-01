package Server;

import Logic.Player;
import Server.websocket.data.DataSend;
import Server.websocket.data.JsonUtil;
import Server.websocket.data.Message;
import Server.websocket.server.Type;

import javax.websocket.EncodeException;
import javax.websocket.Session;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class GameRunnable extends Thread{
    Player p1,p2;
    Session sessionP1,sessionP2;

    Message message;
    ArrayList<AtomicBoolean> hit;
    public GameRunnable(Session p1,Session p2){
        sessionP1 = p1;
        this.p1 = new Player(sessionP1.getId());
        sessionP2 = p2;
        this.p2 = new Player(sessionP2.getId());
        hit.add(new AtomicBoolean(false));
        hit.add(new AtomicBoolean(false));
        message = null;
    }

    public void setMessage(Message message) {
        this.message = message;
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
