package austeretony.oxygen_core.client.gui.overlay;

public interface Overlay {

    boolean valid();

    boolean drawWhileInGUI();

    void draw(float partialTicks);
}
