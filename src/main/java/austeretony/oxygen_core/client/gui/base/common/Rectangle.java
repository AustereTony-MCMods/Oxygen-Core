package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.block.Fill;

import javax.annotation.Nonnull;

public class Rectangle extends Widget<Rectangle> {

    @Nonnull
    protected Fill fill;

    public Rectangle(int x, int y, int width, int height, @Nonnull Fill fill) {
        setPosition(x, y);
        setSize(width, height);
        this.fill = fill;

        setVisible(true);
    }

    public Rectangle(int x, int y, int width, int height) {
        this(x, y, width, height, Fills.def());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());
        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), fill.getColorDefault());
        GUIUtils.popMatrix();
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public void setFill(@Nonnull Fill fill) {
        this.fill = fill;
    }

    @Override
    public String toString() {
        return "Rectangle[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", fill= " + fill.toString() + "]";
    }
}
