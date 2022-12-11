package Server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

import Logic.ConvertNum;
import Server.Websocket.Server.Type;

public class Server {
    private final static int PORT = 1234;
    private final static String HOST = "0.0.0.0";

    private ServerSocket server = null;
    private ArrayList<PlayerOnline> pList = null;
    PlayerOnline p1;
    private PlayerOnline p2;

    public Server() {

        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
            System.out.println("Sever start" + server.toString());
            pList = new ArrayList<>();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
    }

    public String PKT_HELLO(Socket socket) throws IOException {
        byte[] payload = new byte[30];
        int type = -1, len = 0;
        String msv = "";
        byte[] typeBytes;
        byte[] lenBytes;
        byte[] msvBytes;

        socket.getInputStream().read(payload);
        typeBytes = Arrays.copyOfRange(payload, 0, 4);
        type = ConvertNum.fromByteArray(typeBytes);

        if (type == 0) {
            lenBytes = Arrays.copyOfRange(payload, 4, 8);
            len = ConvertNum.fromByteArray(lenBytes);
            msvBytes = Arrays.copyOfRange(payload, 8, 8 + len);
            msv = new String(msvBytes, StandardCharsets.UTF_8);
            System.out.println("MSV: " + msv);
            System.out.println("connected. " + socket.toString());
            // ServerSend.write(pList, null, len);
        } else {
            ServerSend.write(socket, Type.REFUSE_2);
        }
        return msv;
    }

    private boolean acceptClient(Socket socket, String pass) throws IOException {
        boolean res = false;
        byte[] payload = new byte[30];
        int type = -1, len = 0;
        String recvPass = "";
        byte[] typeBytes;
        byte[] lenBytes;
        byte[] recvPassBytes;

        socket.getInputStream().read(payload);
        typeBytes = Arrays.copyOfRange(payload, 0, 4);
        type = ConvertNum.fromByteArray(typeBytes);

        if (type == 0) {
            lenBytes = Arrays.copyOfRange(payload, 4, 8);
            len = ConvertNum.fromByteArray(lenBytes);
            recvPassBytes = Arrays.copyOfRange(payload, 8, 8 + len);
            recvPass = new String(recvPassBytes, StandardCharsets.UTF_8);
            res = (pass == recvPass);
            if (!res)
                ServerSend.write(socket, Type.REFUSE_2);
        }
        return res;
    }

    public void start() {
        // boolean running = true;
        try {
            // add player
            // them nguoi choi 1
            pList.add(new PlayerOnline(server.accept()));
            PKT_HELLO(pList.get(0).getSocket());
            System.out.println("player 1 connected " + pList.get(0).getSocket().getInetAddress().getHostAddress());
            p1 = pList.get(0);
            // them nguoi choi 2
            pList.add(new PlayerOnline(server.accept()));
            PKT_HELLO(pList.get(1).getSocket());
            System.out.println("player 2 connected " + pList.get(1).getSocket().getInetAddress().getHostAddress());
            p2 = pList.get(1);
            // sever da san sang
            // ServerSend.write(sockets.get(0), Type.SERVER_SS, 0);
            // ServerSend.write(sockets.get(1), Type.SERVER_SS, 0);
            ServerSend.write(pList.get(0).getSocket(), Type.SERVER_SS);
            System.out.println("gui thong bao sever san sang cho p1");
            ServerSend.write(pList.get(1).getSocket(), Type.SERVER_SS);
            System.out.println("gui thong bao sever san sang cho p2");

            // cac bien can thiet
            boolean gameEnd = false;
            int idPlayerHit = 0, idPlayerNoHit = 1;
            // thong bao game bat dau
            ServerSend.write(pList.get(0).getSocket(), Type.START);
            System.out.println("gui thong bao game start p1");

            ServerSend.write(pList.get(1).getSocket(), Type.START);
            System.out.println("gui thong bao game start p2");

            // gui ban co cho 2 ng choi
            ServerSend.write(pList.get(0).getSocket(), Type.TABLES, 25, p1.getPlayerTable());
            System.out.println("gui ban co cho p1.\n" + p1.getPlayerTable().toString());
            ServerSend.write(pList.get(1).getSocket(), Type.TABLES, 25, p2.getPlayerTable());
            System.out.println("gui ban co cho p2.\n" + p2.getPlayerTable().toString());
            // game loop
            while (p1.getPlayer().CheckWinCon() == false && p2.getPlayer().CheckWinCon() == false) {
                // pList.get(idPlayerHit).setActivate(true);
                // pList.get(idPlayerNoHit).setActivate(false);
                gameEnd = (p1.getPlayer().CheckWinCon() || p2.getPlayer().CheckWinCon());
                // gui yeu cau danh cho ng choi danh o luot nay
                ServerSend.write(pList.get(idPlayerHit).getSocket(), Type.CLIS, 1, 1);
                // gui yeu cau k danh cho ng choi k danh o luot nay
                // ServerSend.write(pList.get(idPlayerNoHit).getSocket(), Type.CLIS, 1, 2);

                // nhận số đánh của client
                pList.get(idPlayerHit).move();
                pList.get(idPlayerNoHit).move(pList.get(idPlayerHit).getHit());
                // ServerSend.write(pList.get(idPlayerNoHit), Type.TABLES,1,);

                // send hit
                ServerSend.write(pList.get(idPlayerNoHit).getSocket(), Type.HIT, 1, pList.get(idPlayerHit).getHit());
                // ServerSend.write(pList.get(1).getSocket(), Type.TABLES, 25,
                // p2.getCheckTable());

                // send Score cho 2 player
                // ServerSend.write(pList.get(0).getSocket(), Type.SCORE, 1,
                // pList.get(0).getScore());
                // ServerSend.write(pList.get(1).getSocket(), Type.SCORE, 1,
                // pList.get(1).getScore());

                // doi vai tro 2 player

                int tg = idPlayerHit;
                idPlayerHit = idPlayerNoHit;
                idPlayerNoHit = tg;
            }

            if (pList.get(idPlayerNoHit).getPlayer().CheckWinCon()) {
                ServerSend.write(pList.get(idPlayerNoHit).getSocket(), Type.GG, 1, 1);
                ServerSend.write(pList.get(idPlayerHit).getSocket(), Type.GG, 1, 0);
            } else {
                ServerSend.write(pList.get(idPlayerNoHit).getSocket(), Type.GG, 1, 0);
                ServerSend.write(pList.get(idPlayerHit).getSocket(), Type.GG, 1, 1);
            }
            ServerSend.write(p1.getSocket(), Type.SCORE, 1, p1.getScore());
            ServerSend.write(p2.getSocket(), Type.SCORE, 1, p2.getScore());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // public void start(String pass) {

    // try {

    // // them nguoi choi 1
    // sockets.add(server.accept());
    // PKT_HELLO(sockets.get(0));

    // // them nguoi choi 2
    // sockets.add(server.accept());
    // PKT_HELLO(sockets.get(1));

    // // sever da san sang
    // ServerSend.write(sockets.get(0), Type.SERVER_SS, 0);
    // ServerSend.write(sockets.get(1), Type.SERVER_SS, 0);
    // // doi client san sang
    // AtomicInteger p1 = new AtomicInteger(0), p2 = new AtomicInteger(0);

    // // luong cho 2 nguoi choi san sang
    // Thread thread1 = new Thread() {
    // public void run() {
    // byte[] payload = new byte[100];
    // int type = -1;
    // try {
    // sockets.get(0).getInputStream().read(payload);
    // type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));

    // p1.set(type != Type.SERVER_SS.getValue() ? 1 : -1);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // };
    // };

    // Thread thread2 = new Thread() {
    // public void run() {
    // byte[] payload = new byte[100];
    // int type = -1;
    // try {
    // sockets.get(1).getInputStream().read(payload);
    // type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
    // p2.set(type != Type.SERVER_SS.getValue() ? 1 : -1);
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }
    // };
    // };

    // thread1.start();
    // thread2.start();
    // // doi tin hieu ss
    // while (true) {
    // if (p1.get() < 0 || p2.get() < 0) {
    // ServerSend.write(sockets.get(0), Type.REFUSE_2, 0);
    // ServerSend.write(sockets.get(1), Type.REFUSE_2, 0);
    // sockets.get(0).close();
    // sockets.get(1).close();
    // System.exit(1);
    // }
    // if (p1.get() > 0 && p2.get() > 0)
    // break;
    // }
    // // khoi tao game choi
    // Number[][][] banCo = new Number[2][5][5];

    // // cac bien can thiet
    // byte[] payload = new byte[100];
    // ArrayList<Integer> oldNums = new ArrayList<>();
    // running = true;
    // int z;
    // int idPlayerHit = 0, idPlayerNoHit = 1;
    // // thong bao game bat dau
    // ServerSend.write(sockets.get(0), Type.START, 0);
    // ServerSend.write(sockets.get(1), Type.START, 0);
    // // gui ban co cho 2 ng choi
    // ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT, 25,
    // banCo[idPlayerHit]);
    // ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT, 25,
    // banCo[idPlayerNoHit]);

    // // game loop
    // while (running) {
    // // gui yeu cau danh cho ng choi danh o luot nay
    // ServerSend.write(sockets.get(idPlayerHit), Type.CLIENTHIT, 1, 1);
    // // gui yeu cau k danh cho ng choi k danh o luot nay
    // ServerSend.write(sockets.get(idPlayerNoHit), Type.CLIENTHIT, 1, 0);

    // // doc so ma ng choi danh
    // sockets.get(idPlayerHit).getInputStream().read(payload);
    // z = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));

    // }

    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // }

    // private void connectToWeb() {
    // String host = "104.194.240.16";
    // int port = 8881;
    // try {
    // Socket socket = new Socket(host, port);
    // byte[] payload = ConvertNum.intToByteArray(1);

    // } catch (UnknownHostException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // } catch (IOException e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // }

    public static void main(String[] args) {
//        Server.websocket.server.Server server = new Server.websocket.server.Server("pass");
//        Thread thread = new Thread(server);
//        thread.start();
//        while (true){}
    }
}