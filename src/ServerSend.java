import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
// import java.util.Random;

public class ServerSend {

    private static void addPayload(ArrayList<Byte> sendPayload, int value) {
        sendPayload.addAll(Arrays.asList(ConvertNum.toObjects(ConvertNum.reverse(ConvertNum.intToByteArray(value)))));
    }

    private static void addPayload(ArrayList<Byte> sendPayload, String value) {
        sendPayload.addAll(Arrays.asList(ConvertNum.toObjects(value.getBytes(StandardCharsets.UTF_8))));
    }

    public static void write(Socket socket, Type type, int len) throws IOException {
        write(socket, type, len, "");
    }

    public static void write(Socket socket, Type type, int len, int data) throws IOException {
        ArrayList<Byte> sendPayload = new ArrayList<>();
        addPayload(sendPayload, type.getValue());
        addPayload(sendPayload, len);
        addPayload(sendPayload, data);
        write(socket.getOutputStream(), sendPayload);

    }

    public static void write(Socket socket, Type type, int len, Number[][] data) throws IOException {
        ArrayList<Byte> sendPayload = new ArrayList<>();
        addPayload(sendPayload, type.getValue());
        addPayload(sendPayload, len);
        for (int y = 0; y < 5; y++) {
            for (int x = 0; x < 5; x++) {
                addPayload(sendPayload, data[y][x].num);
                addPayload(sendPayload, data[y][x].check);
            }
        }
        write(socket.getOutputStream(), sendPayload);

    }

    public static void write(Socket socket, int type, int len, ArrayList<Integer> data) throws IOException {
        // System.out.println(type);
        ArrayList<Byte> sendPayload = new ArrayList<>();
        addPayload(sendPayload, type);
        addPayload(sendPayload, len);
        for (int i = 0; i < len; i++) {
            addPayload(sendPayload, data.get(i));
        }
        write(socket.getOutputStream(), sendPayload);
    }

    public static void write(Socket socket, Type type, int len, String data) throws IOException {
        // System.out.println(type);
        ArrayList<Byte> sendPayload = new ArrayList<>();
        addPayload(sendPayload, type.getValue());
        addPayload(sendPayload, len);
        if (len > 0) {
            addPayload(sendPayload, data);
        }
        write(socket.getOutputStream(), sendPayload);
    }

    private static void write(OutputStream outputStream, ArrayList<Byte> sendPayload) throws IOException {
        outputStream.write(ConvertNum.toPrimitives(sendPayload.toArray()));
        outputStream.flush();
    }

}
