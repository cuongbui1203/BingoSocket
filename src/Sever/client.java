package Sever;

import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class client {

    static String SERVER_HOST = "127.0.0.1";
    static int PORT = 1234;

    static void bai1() throws IOException {
        Socket socketOfClient;
        try {
            socketOfClient = new Socket(SERVER_HOST, PORT);

            ServerSend.write(socketOfClient, Type.HELLO, 0, "");
            socketOfClient.getInputStream().read();
            socketOfClient.getInputStream().read();
            socketOfClient.getInputStream().read();
            socketOfClient.getInputStream().read();
            socketOfClient.getInputStream().read();
            ServerSend.write(socketOfClient, Type.HIT, 1, 10);
            socketOfClient.getInputStream().read();

        } catch (IOException e) {
            throw new IOException(e);
        }
    }

    public static void main(String[] args) {
        try {
            new client().bai1();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
