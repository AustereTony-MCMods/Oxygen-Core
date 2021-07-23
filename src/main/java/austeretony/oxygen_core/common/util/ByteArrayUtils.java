package austeretony.oxygen_core.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ByteArrayUtils {

    public static byte[] compress(byte[] dataIn) {
        byte[] result = new byte[0];
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(dataIn.length);
             GZIPOutputStream os = new GZIPOutputStream(bos)) {
            os.write(dataIn);
            os.close();

            result = bos.toByteArray();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return result;
    }

    public static byte[] decompress(byte[] dataIn) {
        byte[] result = new byte[0];
        try (ByteArrayInputStream bis = new ByteArrayInputStream(dataIn);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream is = new GZIPInputStream(bis)) {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = is.read(buffer)) != -1) {
                bos.write(buffer, 0, len);
            }

            result = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
