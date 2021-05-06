package austeretony.oxygen_core.client.gui.base.button;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.Layer;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.SliderValueChangeListener;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;
import austeretony.oxygen_core.common.util.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HorizontalSlider extends Widget<HorizontalSlider> {

    protected static final int
            BAR_HEIGHT = 2,
            CARRIAGE_WIDTH = 3,
            CARRIAGE_HEIGHT = 8;

    @Nonnull
    protected Fill carriageFill;
    protected float prevValue, value; // 0 - 1
    protected boolean dragged;

    @Nullable
    protected SliderValueChangeListener instantListener, resultListener;

    public HorizontalSlider(int x, int y, int width, float initialValue) {
        setPosition(x, y);
        setSize(width, CARRIAGE_HEIGHT);
        prevValue = value = MathUtils.clamp(initialValue, 0F, 1F);
        carriageFill = Fills.slider();

        setEnabled(true);
        setVisible(true);
    }

    public HorizontalSlider(int x, int y, int width) {
        this(x, y, width, 0F);
    }

    public HorizontalSlider setInstantValueChangeListener(SliderValueChangeListener listener) {
        instantListener = listener;
        return this;
    }

    public HorizontalSlider setResultValueChangeListener(SliderValueChangeListener listener) {
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

    public void setValue(float value, float from, float to) {
        this.value = (value - from) / (to - from);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int barY = (int) ((getHeight() - BAR_HEIGHT) / 2F);
        GUIUtils.drawRect(0, barY, getWidth(), barY + BAR_HEIGHT,
                CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        GUIUtils.drawFrame(0, barY, getWidth(), BAR_HEIGHT);

        float carriageX = getWidth() * value - ((float) CARRIAGE_WIDTH / 2F);
        GUIUtils.drawRect(carriageX, 0, carriageX + CARRIAGE_WIDTH, getHeight(),
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
        float carriageX = getX() + getWidth() * value - ((float) CARRIAGE_WIDTH / 2F);
        return mouseOver = dragged || (mouseX >= carriageX && mouseY >= getY() && mouseX < carriageX + CARRIAGE_WIDTH
                && mouseY < getY() + getHeight());
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
        value = MathUtils.clamp(adjustMouseX(mouseX) - getX(), 0F, getWidth()) / getWidth();
        if (instantListener != null && from != value) {
            instantListener.change(from, value);
        }
    }

    @Nonnull
    public Fill getCarriageFill() {
        return carriageFill;
    }

    public HorizontalSlider setCarriageFill(@Nonnull Fill fill) {
        carriageFill = fill;
        return this;
    }
}
