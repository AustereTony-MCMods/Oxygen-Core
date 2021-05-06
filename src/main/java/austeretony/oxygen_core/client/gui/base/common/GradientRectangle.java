package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Gradients;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.block.Gradient;

import javax.annotation.Nonnull;

public class GradientRectangle extends Widget<GradientRectangle> {

    @Nonnull
    protected Gradient gradient;

    public GradientRectangle(int x, int y, int width, int height, @Nonnull Gradient gradient) {
        setPosition(x, y);
        setSize(width, height);
        this.gradient = gradient;

        setVisible(true);
    }

    public GradientRectangle(int x, int y, int width, int height) {
        this(x, y, width, height, Gradients.def());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            GUIUtils.pushMatrix();
            GUIUtils.translate(getX(), getY());
                GUIUtils.drawGradientRect(0, 0, getWidth(), getHeight(), gradient.getColorFirst(),
                        gradient.getColorSecond(), gradient.getAlignment());
            GUIUtils.popMatrix();
        }
    }

    @Nonnull
    public Gradient getGradient() {
        return gradient;
    }

    public void setGradient(@Nonnull Gradient gradient) {
        this.gradient = gradient;
    }

    @Override
    public String toString() {
        return "GradientRectangle[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", gradient= " + gradient.toString() + "]";
    }
}
