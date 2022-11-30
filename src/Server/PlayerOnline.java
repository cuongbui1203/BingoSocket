package Server;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import Logic.ConvertNum;
import Logic.Player;

public class PlayerOnline {

    private Socket socket = null;
    private Thread thread = null;

    private InputStream iStream = null;
    private OutputStream oStream = null;
    private AtomicBoolean gameStart;
    private AtomicBoolean activate;
    private AtomicInteger hit;
    private int hit2;
    private Player player;

    private class Process implements Runnable {

        @Override
        public void run() {
            byte[] payload = new byte[100];
            int type = -1;

            try {
                iStream.read(payload);
                type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                hit.set(ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12)));

            } catch (IOException exception) {
                exception.fillInStackTrace();
            }
        }

    }

    PlayerOnline(Socket socket) throws IOException {
        this.socket = socket;
        this.oStream = socket.getOutputStream();
        this.iStream = socket.getInputStream();
        this.activate = new AtomicBoolean(false);
        this.gameStart = new AtomicBoolean(false);
        this.hit = new AtomicInteger(-1);
        this.thread = new Thread(new Process());
        this.player = new Player();
        this.player.setName(socket.toString());
        this.hit2 = 0;
    }

    public ArrayList<Integer> getPlayerTable() {
        return player.getPlayerTable();
    }

    public ArrayList<Integer> getCheckTable() {
        return player.getCheckTable();
    }

    public void move() throws IOException {
        System.out.println("player " + player.getName() + " hit");
        hit.set(0);
        byte[] payload = new byte[100];
        // iStream.read(payload);
        // int type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
        do {
            iStream.read(payload);
            int type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
            hit.set(ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12)));
            hit2 = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));

            if (type != Type.HIT.getValue() || !player.checkMove(hit.get())) {
                ServerSend.write(socket, Type.REFUSE);
            } else {
                player.move(hit.get());

                break;
            }
        } while (true);
    }

    public void move(int n) {

        player.move(n);
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean isActivate() {
        return activate.get();
    }

    public int getScore() {
        return player.getScore();
    }

    public void setActivate(boolean activate) {
        this.activate.set(activate);
        ;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart.set(gameStart);
    }

    public int getHit() {
        int res = hit.get();
        hit.set(-1);
        return hit2;
    }

    public Player getPlayer() {
        return player;
    }

}
