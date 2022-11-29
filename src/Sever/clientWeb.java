package Sever;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import Logic.ConvertNum;

public class clientWeb {
    private void connectToWeb() {
        // String host = "104.194.240.16";
        String host = "127.0.0.1";
        int port = 8081;
        try {
            Socket socket = new Socket(host, port);
            String gameIp = "127.0.0.1";
            int gamePort = 11339;
            String namegame = "Bingo123";
            String author = "nhom 10";
            System.out.println(socket.toString());
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream d = new DataOutputStream(b);
            d.write(ConvertNum.intToByteArray(1));
            d.write(ConvertNum.intToByteArray(gameIp.length()));
            d.write(gameIp.getBytes());
            d.write(ConvertNum.intToByteArray(gamePort));
            // d.writeInt(0);
            d.write(ConvertNum.intToByteArray(namegame.length()));
            d.write(namegame.getBytes());
            d.write(ConvertNum.intToByteArray(1));
            d.write("1".getBytes());
            d.write(ConvertNum.intToByteArray(author.length()));
            d.write(author.getBytes());
            d.flush();
            System.out.println(Arrays.toString(b.toByteArray()));
            socket.getOutputStream().write(b.toByteArray());
            socket.getOutputStream().flush();
            // socket.
            byte[] payload = new byte[100];

            socket.getInputStream().read(payload);
            System.out.println(Arrays.toString(payload));
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        new clientWeb().connectToWeb();
    }
}
