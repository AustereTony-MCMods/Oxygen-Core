package austeretony.oxygen_core.common.util.objects;

public class Point2F {

    private final float x, y;

    public Point2F(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public static Point2F of(float x, float y) {
        return new Point2F(x, y);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
