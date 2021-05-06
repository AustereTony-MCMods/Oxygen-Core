package austeretony.oxygen_core.common.util.objects;

public class Point2D {

    private final double x, y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Point2D of(double x, double y) {
        return new Point2D(x, y);
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
