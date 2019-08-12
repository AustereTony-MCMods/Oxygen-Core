package austeretony.oxygen.client.gui.overlay;

public interface IOverlay {

    boolean shouldDraw();

    default boolean drawWhileInGUI() {
        return false;
    }

    void draw(float partialTicks);
}
