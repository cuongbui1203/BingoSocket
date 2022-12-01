package Logic;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class ConvertNum {

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
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream d = new DataOutputStream(b);
            d.writeInt(value);
            d.flush();
            return reverse(b.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] concatArray(byte[] array1, byte[] array2) {
        byte[] result = Arrays.copyOf(array1, array1.length + array2.length);
        System.arraycopy(array2, 0, result, array1.length, array2.length);
        return result;
    }

    public static final byte[] numberToByteArray(Number val) {
        return concatArray(intToByteArray(val.num), intToByteArray(val.check));
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
