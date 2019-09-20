package austeretony.oxygen_core.common.util;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BufferedImageUtils {

    public static final int
    BYTE_ARRAY_LENGTH = 16384,
    INT_ARRAY_LENGTH = 4096;

    public static List<byte[]> convertBufferedImageToByteArraysList(BufferedImage bufferedImage) {
        byte[] imageArray = ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
        return divideArray(imageArray, BYTE_ARRAY_LENGTH);  
    }

    public static BufferedImage convertByteArraysListToBufferedImage(List<byte[]> imageByteParts, int imageWidth, int imageHeight) {
        byte[] imageArray;
        try {
            imageArray = mergeByteArrays(imageByteParts);
        } catch (IOException exception) {
            exception.printStackTrace();
            return null;
        }               
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_3BYTE_BGR);
        bufferedImage.setData(Raster.createRaster(bufferedImage.getSampleModel(), new DataBufferByte(imageArray, imageArray.length), new Point()));
        return bufferedImage;
    }

    public static List<int[]> convertBufferedImageToIntArraysList(BufferedImage bufferedImage) {
        int[] imageArray = ((DataBufferInt) bufferedImage.getRaster().getDataBuffer()).getData();                          
        return divideArray(imageArray, INT_ARRAY_LENGTH);           
    }

    public static BufferedImage convertIntArraysListToBufferedImage(List<int[]> imageIntParts, int imageWidth, int imageHeight) {
        int[] imageArray = mergeIntArrays(imageIntParts);               
        BufferedImage bufferedImage = new BufferedImage(imageWidth, imageWidth, BufferedImage.TYPE_INT_RGB);
        bufferedImage.setData(Raster.createRaster(bufferedImage.getSampleModel(), new DataBufferInt(imageArray, imageArray.length), new Point()));
        return bufferedImage;
    }

    public static List<byte[]> divideArray(byte[] array, int size) {
        List<byte[]> result = new ArrayList<byte[]>();
        int 
        start = 0,
        end;
        while (start < array.length) {
            end = Math.min(array.length, start + size);
            result.add(Arrays.copyOfRange(array, start, end));
            start += size;
        }
        return result;
    }

    public static List<int[]> divideArray(int[] array, int size) {
        List<int[]> result = new ArrayList<int[]>();
        int 
        start = 0,
        end;
        while (start < array.length) {
            end = Math.min(array.length, start + size);
            result.add(Arrays.copyOfRange(array, start, end));
            start += size;
        }
        return result;
    }

    public static byte[] mergeByteArrays(List<byte[]> arrays) throws IOException {
        byte[] result;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) { 
            for (byte[] array : arrays) {
                try {
                    outputStream.write(array);
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
            }
            result = outputStream.toByteArray();
        } 
        return result;
    }

    public static int[] mergeIntArrays(List<int[]> arrays) {
        int 
        size = 0, 
        length;
        for (int[] a : arrays)
            size += a.length;
        int[] result = new int[size];
        int destPos = 0;
        for (int i = 0; i < arrays.size(); i++) {
            if (i > 0) 
                destPos += arrays.get(i - 1).length;
            length = arrays.get(i).length;
            System.arraycopy(arrays.get(i), 0, result, destPos, length);
        }
        return result;
    }
}
