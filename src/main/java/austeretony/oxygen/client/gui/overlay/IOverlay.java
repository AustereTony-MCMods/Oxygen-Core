package austeretony.oxygen.client.gui.overlay;

public interface IOverlay {

    boolean shouldDraw();

    void draw(float partialTicks);
}
