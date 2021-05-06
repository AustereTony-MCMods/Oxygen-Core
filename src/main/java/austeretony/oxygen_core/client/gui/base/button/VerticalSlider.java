package austeretony.oxygen_core.client.gui.base.button;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.SliderValueChangeListener;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;
import austeretony.oxygen_core.common.util.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class VerticalSlider extends Widget<VerticalSlider> {

    private static final int MIN_CARRIAGE_HEIGHT = 10;

    @Nonnull
    protected Fill carriageFill;
    protected float carriageHeight;

    protected float prevValue, value; // 0 - 1
    protected boolean dragged;

    @Nullable
    protected SliderValueChangeListener instantListener, resultListener;

    public VerticalSlider(int x, int y, int width, int height, @Nonnull Fill fill, float initialValue) {
        setPosition(x, y);
        setSize(width, height);
        carriageFill = fill;
        prevValue = value = MathUtils.clamp(initialValue, 0F, 1F);

        setEnabled(true);
        setVisible(true);
    }

    public VerticalSlider(int x, int y, int width, int height) {
        this(x, y, width, height, Fills.slider(), 0F);
    }

    public VerticalSlider setInstantValueChangeListener(SliderValueChangeListener listener) {
        instantListener = listener;
        return this;
    }

    public VerticalSlider setResultValueChangeListener(SliderValueChangeListener listener) {
        resultListener = listener;
        return this;
    }

    public float getValue() {
        return value;
    }

    public float calculateValue(float from, float to) {
        return from + (to - from) * getValue();
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void calculateCarriageSize(int visible, int max) {
        prevValue = value = 0F;
        carriageHeight = max < visible ? getHeight() : getHeight() / ((float) max / visible);
        if (carriageHeight < MIN_CARRIAGE_HEIGHT) carriageHeight = MIN_CARRIAGE_HEIGHT;
        if (carriageHeight > getHeight()) carriageHeight = getHeight();
    }

    public void updateCarriagePosition(int visible, int max, int current) {
        prevValue = value = MathUtils.clamp(current / ((float) max - visible), 0F, 1F);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || carriageHeight == getHeight()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        float carriageY = value * (getHeight() - carriageHeight);
        if (Float.isNaN(carriageY)) carriageY = 0F;
        GUIUtils.drawRect(0, carriageY, getWidth(), carriageY + carriageHeight,
                getColorFromState(carriageFill));

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        if (!isEnabled()) return false;
        if (getLayer() == Layer.MIDDLE) {
            mouseX -= getScreen().getWorkspace().getX();
            mouseY -= getScreen().getWorkspace().getY();
        }
        float carriageY = getY() + value * (getHeight() - carriageHeight);
        return mouseOver = dragged || (mouseX >= getX() && mouseY >= carriageY && mouseX < getX() + getWidth()
                && mouseY < carriageY + carriageHeight);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            prevValue = value;
            dragged = true;
            return true;
        }
        return false;
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        if (!isEnabled()) return;
        if (dragged && resultListener != null) {
            resultListener.change(prevValue, value);
        }
        dragged = false;
    }

    @Override
    public void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (!isEnabled() || !isMouseOver() || clickedMouseButton != MouseButtons.LEFT_BUTTON || !dragged) return;
        float from = value;
        value = MathUtils.clamp(adjustMouseY(mouseY) - getY(), 0F, getHeight()) / getHeight();
        if (instantListener != null && from != value) {
            instantListener.change(from, value);
        }
    }

    @Nonnull
    public Fill getCarriageFill() {
        return carriageFill;
    }

    public VerticalSlider setCarriageFill(@Nonnull Fill fill) {
        carriageFill = fill;
        return this;
    }

    @Override
    public String toString() {
        return "VerticalSlider[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", fill= " + carriageFill.toString() + "]";
    }
}
