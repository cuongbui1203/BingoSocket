import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

public class Server {
    private final static int PORT = 1234;
    private final static String HOST = "0.0.0.0";

    private ServerSocket server = null;
    private ArrayList<Socket> sockets = null;
    private boolean running;
    private ArrayList<PlayerOnline> pList = null;
    private Player p1, p2;

    public Server() {
        p1 = new Player();
        p2 = new Player();
        running = false;
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
            System.out.println(server.toString());
            sockets = new ArrayList<>();
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

    public void process() {
        // boolean running = true;
        try {
            // add player
            // them nguoi choi 1
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(0));
            ServerSend.write(pList.get(0).getSocket(), Type.SERVER_SS);
            System.out.println("gui thong bao sever san sang cho p1");

            // them nguoi choi 2
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(1));

            // sever da san sang
            // ServerSend.write(sockets.get(0), Type.SERVER_SS, 0);
            // ServerSend.write(sockets.get(1), Type.SERVER_SS, 0);
            ServerSend.write(pList.get(1).getSocket(), Type.SERVER_SS);
            System.out.println("gui thong bao sever san sang cho p2");

            // doi client san sang
            pList.get(0).setActivate(true);
            pList.get(1).setActivate(false);
            // System.out.println("");

            // luong cho 2 nguoi choi san sang

            // doi tin hieu ss

            // khoi tao game choi

            // cac bien can thiet
            boolean gameEnd = false;
            int z;
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
            while (!gameEnd) {
                pList.get(idPlayerHit).setActivate(true);
                pList.get(idPlayerNoHit).setActivate(false);
                // gui yeu cau danh cho ng choi danh o luot nay
                ServerSend.write(pList.get(idPlayerHit).getSocket(), Type.CLIS, 1, 1);
                // gui yeu cau k danh cho ng choi k danh o luot nay
                ServerSend.write(pList.get(idPlayerNoHit).getSocket(), Type.CLIS, 1, 0);

                // nhận số đánh của client
                pList.get(idPlayerHit).move();
                pList.get(idPlayerNoHit).move(pList.get(idPlayerHit).getHit());

                // send information check table
                ServerSend.write(pList.get(0).getSocket(), Type.TABLES, 25, p1.getPlayerTable());
                ServerSend.write(pList.get(1).getSocket(), Type.TABLES, 25, p2.getPlayerTable());

                // send Score cho 2 player
                ServerSend.write(pList.get(0).getSocket(), Type.SCORE, 1, pList.get(0).getSocre());
                ServerSend.write(pList.get(1).getSocket(), Type.SCORE, 1, pList.get(1).getSocre());

                // doi vai tro 2 player
                {
                    int tg = idPlayerHit;
                    idPlayerHit = idPlayerNoHit;
                    idPlayerNoHit = tg;
                }

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    // public void process(String pass) {

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

    public static void main(String[] args) {
        new Server().process();
    }
}