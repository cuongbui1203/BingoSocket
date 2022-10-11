import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

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
            ServerRecv.write(socket, Type.REFUSE_2.getValue(), 0);
        }
        return msv;
    }

    public void process() {
        try {
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(0));
            
            sockets.add(server.accept());
            PKT_HELLO(sockets.get(1));

            ServerRecv.write(sockets.get(0), Type.SERVER_SS.getValue(), 0);
            ServerRecv.write(sockets.get(1), Type.SERVER_SS.getValue(), 0);
            
            AtomicInteger p1 = new AtomicInteger(0), p2 = new AtomicInteger(0);

            Thread thread1 = new Thread(){
                public void run() {
                    byte[] payload = new byte[100];
                    int type = -1;
                    try {
                        sockets.get(0).getInputStream().read(payload);
                        type = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                        
                        p1.set(type != Type.SERVER_SS.getValue() ? 1:-1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            };

            Thread thread2 = new Thread(){
                public void run() {
                    byte[] payload = new byte[100];
                    int type = -1;
                    try {
                        sockets.get(1).getInputStream().read(payload);
                        type = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                        p2.set(type != Type.SERVER_SS.getValue() ? 1:-1);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                };
            };

            thread1.start();
            thread2.start();
            while(true){
                if(p1.get()<0||p2.get()<0){
                    ServerRecv.write(sockets.get(0), Type.REFUSE_2.getValue(),0);
                    ServerRecv.write(sockets.get(1), Type.REFUSE_2.getValue(),0);
                    sockets.get(0).close();
                    sockets.get(1).close();
                }
                if(p1.get() > 0 && p2.get() > 0) break;
            }
            int[][][] banCo = new int[2][5][5];
            byte[] payload = new byte[100];
            boolean gameEnd = false;
            int x,y,z;
            int idPlayerHit = 0,idPlayerNoHit = 1;
            ServerRecv.write(sockets.get(0), Type.START.getValue(), 0);
            ServerRecv.write(sockets.get(1), Type.START.getValue(), 0);
            while(!gameEnd){
                ServerRecv.write(sockets.get(idPlayerHit), Type.CLIS.getValue(), 1,1);
                ServerRecv.write(sockets.get(idPlayerNoHit), Type.CLIS.getValue(), 1,0);
                sockets.get(idPlayerHit).getInputStream().read(payload);
                x = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));
                y = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 12, 16));
                z = ConverNum.fromByteArray(Arrays.copyOfRange(payload, 16, 20));
                
                if(banCo[idPlayerHit][y][x] != 0 || z < 1 || z > 25){
                    ServerRecv.write(sockets.get(idPlayerHit), Type.REFUSE.getValue(), 0);
                } else{
                    banCo[idPlayerHit][y][x] = z;
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

}