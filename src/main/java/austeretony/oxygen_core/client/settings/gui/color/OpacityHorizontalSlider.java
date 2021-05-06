package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;

public class OpacityHorizontalSlider extends HorizontalSlider {

    protected final int firstColorHex;
    protected int secondColorHex;

    public OpacityHorizontalSlider(int x, int y, int width, float alphaValue, int colorHex) {
        super(x, y, width, alphaValue);
        setSize(width, 6);
        firstColorHex = (colorHex & 0xFFFFFF) | 0;
        secondColorHex = (colorHex & 0xFFFFFF) | (255 << 24);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int barY = (int) ((getHeight() - BAR_HEIGHT) / 2F);
        GUIUtils.drawGradientRect(0, barY, getWidth(), barY + BAR_HEIGHT,
                firstColorHex, getBarSecondColor(), Alignment.RIGHT);
        GUIUtils.drawFrame(0, barY, getWidth(), BAR_HEIGHT);

        float carriageX = getWidth() * value - ((float) CARRIAGE_WIDTH / 2F);
        //int carriageColor = calculateCarriageColor();
        GUIUtils.drawRect(carriageX, 0, carriageX + CARRIAGE_WIDTH, getHeight(),
                getColorFromState(getCarriageFill()));
        //Reference.drawFrame(carriageX, 0, CARRIAGE_WIDTH, getHeight());

        GUIUtils.popMatrix();
    }

    private int calculateCarriageColor() {
        return (secondColorHex & 0xFFFFFF) | ((int) (255F * getValue()) << 24);
    }

    public int getBarSecondColor() {
        return secondColorHex;
    }

    public void setBarSecondColor(int color) {
        secondColorHex = color;
    }
}
