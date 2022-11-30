package Server;

import java.io.IOException;
import java.net.Socket;
import java.security.Principal;
import java.util.Arrays;
import java.util.Scanner;

import Logic.ConvertNum;

public class Client {
    static final String SERVER_HOST = "0.0.0.0";
    static final int PORT = 1234;

    int[] read(byte[] payload) {
        int n = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 4, 8));
        int[] res = new int[n];
        for (int i = 0; i < n; i++) {
            int tg = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8 + i * 4, 12 + i * 4));
            res[i] = tg;
        }
        return res;
    }

    void getData() throws IOException {
        Scanner scanner = new Scanner(System.in);

        Socket socketOfClient;
        try {
            socketOfClient = new Socket(SERVER_HOST, PORT);

            ServerSend.write(socketOfClient, Type.HELLO, 0, "");

            while (true) {
                byte[] payload = new byte[130];

                socketOfClient.getInputStream().read(payload);
                System.out.println(Arrays.toString(payload));
                if (ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4)) == Type.START.getValue()) {
                    System.out.println("end read");
                    break;
                }

            }

            System.out.println("start");

            while (true) {
                // 1
                byte[] payload = new byte[130];

                socketOfClient.getInputStream().read(payload);
                int type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));

                if (ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4)) == Type.TABLES.getValue()) {
                    System.out.println(Arrays.toString(read(payload)));
                    // break;
                }

                if (type == Type.CLIS.getValue()) {

                    // hit
                    int n = 0;
                    do {

                        System.out.println("nhap nuoc di cua ban: ");
                        n = scanner.nextInt();
                        ServerSend.write(socketOfClient, Type.HIT, 1, n);
                        socketOfClient.getInputStream().read(payload);
                        type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                        // ServerSend.write(socketOfClient, Type.HIT,1,);
                    } while (type == Type.REFUSE.getValue());
                    // n = scanner.nextInt();
                    // System.out.println("ok");

                }
                if (type == Type.HIT.getValue()) {
                    System.out.println("hit");
                    // socketOfClient.getInputStream().read(payload);

                    // type = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 0, 4));
                    // System.out.println(type);
                    // if (type == Type.HIT.getValue()) {
                    int[] check = new int[25];
                    // socketOfClient.getInputStream().read(payload);
                    System.out.println(ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12)));
                    // System.out.println(Arrays.toString(payload));

                    // }
                }
                if (type == Type.GG.getValue()) {
                    int t = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));
                    if (t == 1)
                        System.out.println("you win");
                    else
                        System.out.println("you lose");

                }
                if (type == Type.SCORE.getValue()) {

                    int score = ConvertNum.fromByteArray(Arrays.copyOfRange(payload, 8, 12));
                    System.out.println("you score: " + score);
                }

            }

        } catch (IOException e) {
            System.out.println("Error when client get data: " + e);
            throw new IOException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Start");

        try {
            new Client().getData();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("Error: " + e);
            e.printStackTrace();
        }
    }
}