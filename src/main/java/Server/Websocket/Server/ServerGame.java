package Server.Websocket.Server;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class ServerGame implements Runnable {
    public ServerGame(String password, int math, String p1Name, String p2Name){
        pass = password;
        this.port = new Random().nextInt(9999,12450);
        ServerGameEndpoint.math = math;
        ServerGameEndpoint.p1Name = p1Name;
        ServerGameEndpoint.p2Name = p2Name;
    }
    private int port;
    static public String pass;

    public static void main(String[] args) {
        ServerGame server = new ServerGame("password",509,"1","2");
        Thread thread = new Thread(server);
        thread.start();
    }

    @Override
    public void run() {
        ServerGameEndpoint.password = pass;
//        ServerGameEndpoint.PORT = port;
        ServerGameEndpoint.PORT = 10637;
        try {
            ServerGameEndpoint.socket = new Socket("localhost",12346);
            System.out.println("Connect to py Socket thanh cong");
        } catch (IOException e) {
            System.out.println(Arrays.toString(new RuntimeException(e).getStackTrace()));

        }
        org.glassfish.tyrus.server.Server server = new org.glassfish.tyrus.server.Server("localhost",
                port,
                "/game",
                ServerGameEndpoint.class);


        try {
            server.start();
            System.out.println("Player 1 name: "+ ServerGameEndpoint.p1Name);
            System.out.println("Player 2 name: "+ ServerGameEndpoint.p2Name);
            System.out.println("Password: "+ ServerGameEndpoint.password);
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