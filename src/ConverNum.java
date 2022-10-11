import java.util.Arrays;

public class ConverNum {
    
    public static byte[] reverse(byte[] bytes) {
        int i;
        byte t;
        int n = bytes.length;
        for (i = 0; i < n / 2; i++) {
            t = bytes[i];
            bytes[i] = bytes[n - i - 1];
            bytes[n - i - 1] = t;
        }
        return bytes;
    }

    public static int fromByteArray(byte[] bytes) {
        reverse(bytes);
        return bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF);
    }

    public static final byte[] intToByteArray(int value) {
        byte[] res;
        res = new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
        // reverse(res);
        return res;
    }

    public static Byte[] toObjects(byte[] bytesPrim) {
        Byte[] bytes = new Byte[bytesPrim.length];
        Arrays.setAll(bytes, n -> bytesPrim[n]);
        return bytes;
    }

    public static byte[] toPrimitives(Object[] objects) {
        byte[] bytes = new byte[objects.length];
        for (int i = 0; i < objects.length; i++)
            bytes[i] = (Byte) objects[i];
        return bytes;
    }

}
