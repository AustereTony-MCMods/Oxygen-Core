package austeretony.oxygen.util;

public class MathUtils {

    //*** clamp

    public static int clamp(int value, int min, int max) {
        if (value < min)
            return min;
        else
            return value > max ? max : value;
    }

    public static long clamp(long value, long min, long max) {
        if (value < min)
            return min;
        else
            return value > max ? max : value;
    }

    public static float clamp(float value, float min, float max) {
        if (value < min)
            return min;
        else
            return value > max ? max : value;
    }

    public static double clamp(double value, double min, double max) {
        if (value < min)
            return min;
        else
            return value > max ? max : value;
    }

    //*** lesserOfTwo

    public static int lesserOfTwo(int first, int second) {
        if (first < second)
            return first;
        else
            return second;
    }

    public static long lesserOfTwo(long first, long second) {
        if (first < second)
            return first;
        else
            return second;
    }

    public static float lesserOfTwo(float first, float second) {
        if (first < second)
            return first;
        else
            return second;
    }

    public static double lesserOfTwo(double first, double second) {
        if (first < second)
            return first;
        else
            return second;
    }

    //*** greaterOfTwo

    public static int greaterOfTwo(int first, int second) {
        if (first > second)
            return first;
        else
            return second;
    }

    public static long greaterOfTwo(long first, long second) {
        if (first > second)
            return first;
        else
            return second;
    }

    public static float greaterOfTwo(float first, float second) {
        if (first > second)
            return first;
        else
            return second;
    }

    public static double greaterOfTwo(double first, double second) {
        if (first > second)
            return first;
        else
            return second;
    }

    //*** percentValueOf

    public static int percentValueOf(int value, int percent) {
        return (int) (((float) value / 100.0F) * (float) percent);
    }

    public static long percentValueOf(long value, int percent) {
        return (long) (((double) value / 100.0D) * (double) percent);
    }

    public static float percentValueOf(float value, int percent) {
        return (value / 100.0F) * (float) percent;
    }

    public static double percentValueOf(double value, int percent) {
        return (value / 100.0D) * (double) percent;
    }
}