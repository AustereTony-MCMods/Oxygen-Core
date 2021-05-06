package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.Widget;

public class ColorWidget extends Widget<ColorWidget> {

    protected int colorHex;

    public ColorWidget(int x, int y, int colorHex) {
        setPosition(x, y);
        setSize(9, 9);
        this.colorHex = colorHex;

        setVisible(true);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), colorHex);
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight());

        GUIUtils.popMatrix();
    }

    public int getColor() {
        return colorHex;
    }

    public void setColor(int color) {
        colorHex = color;
    }
}
