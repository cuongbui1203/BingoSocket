package Server.Websocket.Server;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.Socket;
import java.util.Random;
import java.util.Scanner;

public class Server implements Runnable {
    public Server(String password,int math,String p1Name,String p2Name){
        pass = password;
        this.port = new Random().nextInt(9999,12450);
        ServerEndpoint.math = math;
        ServerEndpoint.p1Name = p1Name;
        ServerEndpoint.p2Name = p2Name;
    }
    private int port;
    static public String pass;

    public static void main(String[] args) {
//        ServerEndpoint.password = pass;
//        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost",
//                8887,
//                "/game",
//                ServerEndpoint.class);
//
//        try {
//            server.start();
//            System.out.println("Press any key to stop the server..");
//            new Scanner(System.in).nextLine();
//        } catch (DeploymentException e) {
//            throw new RuntimeException(e);
//        } finally {
//            server.stop();
//        }
        Server server = new Server("",1,"1","2");
        Thread thread = new Thread(server);

        thread.start();
    }

    @Override
    public void run() {
        ServerEndpoint.password = pass;
//        ServerEndpoint.PORT = port;
        ServerEndpoint.PORT = 10637;
        try {
            ServerEndpoint.socket = new Socket("localhost",12346);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost",
                10637,
                "/game",
                ServerEndpoint.class);
        try {
            server.start();
            System.out.println("Press any key to stop the server..");
            new Scanner(System.in).nextLine();
        } catch (DeploymentException e) {
            throw new RuntimeException(e);
        } finally {
            server.stop();
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}