package austeretony.oxygen_core.common.util;

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