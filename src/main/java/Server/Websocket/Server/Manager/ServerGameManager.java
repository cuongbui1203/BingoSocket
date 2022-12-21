package Server.Websocket.Server.Manager;

import org.glassfish.tyrus.server.Server;


import javax.websocket.DeploymentException;
import java.util.Scanner;

public class ServerGameManager {
    static final int PORT = 12345;
    public static void main(String[] args) {
        Server server = new Server(
                "0.0.0.0",
                PORT,
                "/",
                ServerEndpoint2.class
        );

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

}