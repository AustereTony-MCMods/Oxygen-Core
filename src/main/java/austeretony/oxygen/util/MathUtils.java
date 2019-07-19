package austeretony.oxygen.util;

public class MathUtils {

    public static int clamp(int num, int min, int max) {
        if (num < min)
            return min;
        else
            return num > max ? max : num;
    }

    public static float clamp(float num, float min, float max) {
        if (num < min)
            return min;
        else
            return num > max ? max : num;
    }

    public static double clamp(double num, double min, double max) {
        if (num < min)
            return min;
        else
            return num > max ? max : num;
    }
}
