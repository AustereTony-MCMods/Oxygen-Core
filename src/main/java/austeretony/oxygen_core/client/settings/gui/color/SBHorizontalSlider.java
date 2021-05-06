package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;

public class SBHorizontalSlider extends HorizontalSlider {

    protected final int firstColorHex;
    protected int secondColorHex;

    public SBHorizontalSlider(int x, int y, int width, float sbValue, int firstColorHex, int secondColorHex) {
        super(x, y, width, sbValue);
        setSize(width, 6);
        this.firstColorHex = firstColorHex;
        this.secondColorHex = secondColorHex;
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
        int first = firstColorHex;
        int second = getBarSecondColor();

        int firstR = first >> 16 & 0xFF;
        int firstG = first >> 8 & 0xFF;
        int firstB = first & 0xFF;

        int secondR = second >> 16 & 0xFF;
        int secondG = second >> 8 & 0xFF;
        int secondB = second & 0xFF;

        float ratio = 1F - getValue();
        int carriageR = (int) Math.sqrt(Math.pow(firstR, 2) * ratio + Math.pow(secondR, 2) * (1 - ratio));
        int carriageG = (int) Math.sqrt(Math.pow(firstG, 2) * ratio + Math.pow(secondG, 2) * (1 - ratio));
        int carriageB = (int) Math.sqrt(Math.pow(firstB, 2) * ratio + Math.pow(secondB, 2) * (1 - ratio));
        return ((255 & 0xFF) << 24) | ((carriageR & 0xFF) << 16) | ((carriageG & 0xFF) << 8) | (carriageB & 0xFF);
    }

    public int getBarSecondColor() {
        return secondColorHex;
    }

    public void setBarSecondColor(int color) {
        secondColorHex = color;
    }
}
