package austeretony.oxygen_core.client.gui.overlay;

public interface Overlay {

    boolean shouldDraw();

    boolean drawWhileInGUI();

    void draw(float partialTicks);
}