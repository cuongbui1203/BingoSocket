package Server.Websocket.Server.Web;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.DeploymentException;
import javax.websocket.Session;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class Client {

    static final String SERVER = "ws://0.0.0.0:8887/ws/channels/";

    public static void main(String[] args) throws URISyntaxException, DeploymentException, IOException {
        ClientManager clientManager = ClientManager.createClient();
        Session s = clientManager.connectToServer(ClientEndpoint.class,new URI(SERVER));
        System.out.println("connect");
    }
}
