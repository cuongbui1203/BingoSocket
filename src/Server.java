import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.util.Pair;

public class Server {
    private final static int PORT = 1234;
    private final static String HOST = "0.0.0.0";

    private ServerSocket server = null;
    private ArrayList<Socket> sockets = null;

    public Server() {
        try {
            server = new ServerSocket();
            server.bind(new InetSocketAddress(InetAddress.getByName(HOST), PORT));
            sockets = new ArrayList<>();
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
        type = ConverNum.fromByteArray(typeBytes);

        if (type == 0) {
            lenBytes = Arrays.copyOfRange(payload, 4, 8);
            len = ConverNum.fromByteArray(lenBytes);
            msvBytes = Arrays.copyOfRange(payload, 8, 8 + len);
            msv = new String(msvBytes, StandardCharsets.UTF_8);
            System.out.println("MSV: " + msv);
        } else {
            ServerSend.write(socket, Type.REFUSE_2.getValue(), 0);
        }
        return msv;
    }

    private Pair<Integer, Integer> find(Number[][] aNumbers, int val) {
        Pair<Integer, Integer> res = null;
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                if (aNumbers[y][x].num == val) {
                    res = new Pair<Integer, Integer>(x, y);
                    break;
                }
            }
        }
        return res;
    }

    public void process() {
        try {
            // them nguoi choi 1
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(0));

            // them nguoi choi 2
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(1));

            // sever da san sang
            ServerSend.write(sockets.get(0), Type.SERVER_SS.getValue(), 0);
            ServerSend.write(sockets.get(1), Type.SERVER_SS.getValue(), 0);
            // doi client san sang
            AtomicInteger p1 = new AtomicInteger(0), p2 = new AtomicInteger(0);

            // luong cho 2 nguoi choi san sang
            Thread thread1 = new Thread() {
                public void run() {
                    byte[] payload = new byte[100];
                    int type = -1;
                    try {
                        sockets.get(0).getInputStream().read(payload);
                        type = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));

                        p1.set(type != Type.SERVER_SS.getValue() ? 1 : -1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            };

            Thread thread2 = new Thread() {
                public void run() {
                    byte[] payload = new byte[100];
                    int type = -1;
                    try {
                        sockets.get(1).getInputStream().read(payload);
                        type = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                        p2.set(type != Type.SERVER_SS.getValue() ? 1 : -1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            };

            thread1.start();
            thread2.start();
            // doi tin hieu ss
            while (true) {
                if (p1.get() < 0 || p2.get() < 0) {
                    ServerSend.write(sockets.get(0), Type.REFUSE_2.getValue(), 0);
                    ServerSend.write(sockets.get(1), Type.REFUSE_2.getValue(), 0);
                    sockets.get(0).close();
                    sockets.get(1).close();
                    System.exit(1);
                }
                if (p1.get() > 0 && p2.get() > 0)
                    break;
            }
            // khoi tao game choi
            Number[][][] banCo = new Number[2][5][5];

            // cac bien can thiet
            byte[] payload = new byte[100];
            ArrayList<Integer> oldNums = new ArrayList<>();
            boolean gameEnd = false;
            int z;
            int idPlayerHit = 0, idPlayerNoHit = 1;
            // thong bao game bat dau
            ServerSend.write(sockets.get(0), Type.START.getValue(), 0);
            ServerSend.write(sockets.get(1), Type.START.getValue(), 0);
            // gui ban co cho 2 ng choi
            ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT.getValue(), 25, banCo[idPlayerHit]);
            ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT.getValue(), 25, banCo[idPlayerNoHit]);

            // game loop
            while (!gameEnd) {
                // gui yeu cau danh cho ng choi danh o luot nay
                ServerSend.write(sockets.get(idPlayerHit), Type.CLIS.getValue(), 1, 1);
                // gui yeu cau k danh cho ng choi k danh o luot nay
                ServerSend.write(sockets.get(idPlayerNoHit), Type.CLIS.getValue(), 1, 0);

                // doc so ma ng choi danh
                sockets.get(idPlayerHit).getInputStream().read(payload);
                z = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));

                if (z < 1 || z > 25 || oldNums.indexOf(z) != -1) {
                    ServerSend.write(sockets.get(idPlayerHit), Type.REFUSE.getValue(), 0);
                    continue;
                } else {
                    Pair<Integer, Integer> tg1 = find(banCo[idPlayerHit], z);
                    banCo[idPlayerHit][tg1.getValue()][tg1.getKey()].check = 1;
                    tg1 = find(banCo[idPlayerNoHit], z);
                    banCo[idPlayerNoHit][tg1.getValue()][tg1.getKey()].check = 1;

                    ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT.getValue(), 25, banCo[idPlayerHit]);
                    ServerSend.write(sockets.get(idPlayerHit), Type.ACCEPT.getValue(), 25, banCo[idPlayerNoHit]);

                    oldNums.add(z);
                    int tg = idPlayerHit;
                    idPlayerHit = idPlayerNoHit;
                    idPlayerNoHit = tg;
                }
                if (CheckWinCondition.CheckWin(banCo[idPlayerHit])) {
                    ServerSend.write(sockets.get(idPlayerHit), Type.GG.getValue(), 1, 1);
                    ServerSend.write(sockets.get(idPlayerNoHit), Type.GG.getValue(), 1, 0);
                    gameEnd = true;
                }
                if (CheckWinCondition.CheckWin(banCo[idPlayerNoHit])) {
                    ServerSend.write(sockets.get(idPlayerHit), Type.GG.getValue(), 1, 0);
                    ServerSend.write(sockets.get(idPlayerNoHit), Type.GG.getValue(), 1, 1);
                    gameEnd = true;
                }

            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}