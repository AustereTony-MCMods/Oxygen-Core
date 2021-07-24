package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Textures;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Sorter extends Widget<Sorter> {

    @Nonnull
    protected Texture downTexture, upTexture;
    @Nonnull
    protected String tooltip;
    @Nonnull
    protected State state;

    @Nullable
    protected StateChangeListener listener;

    public Sorter(int x, int y, @Nonnull State state, @Nonnull String tooltip) {
        setPosition(x, y);
        setSize(3, 3);
        this.state = state;
        this.tooltip = tooltip;
        downTexture = Texture.builder()
                .texture(Textures.SORT_DOWN_ICONS)
                .size(3, 3)
                .imageSize(9, 3)
                .build();
        upTexture = Texture.builder()
                .texture(Textures.SORT_UP_ICONS)
                .size(3, 3)
                .imageSize(9, 3)
                .build();

        setEnabled(true);
        setVisible(true);
    }

    public Sorter(int x, int y, @Nonnull State state) {
        this(x, y, state, "");
    }

    public Sorter setStateChangeListener(StateChangeListener listener) {
        this.listener = listener;
        return this;
    }

    @Nonnull
    public State getState() {
        return state;
    }

    public void setState(@Nonnull State state) {
        this.state = state;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        Texture texture = downTexture;
        switch (state) {
            case UP:
                texture = upTexture;
                break;
            case DOWN:
            case INACTIVE:
                texture = downTexture;
                break;
        }

        int iconU = texture.getU();
        if (!isEnabled())
            iconU += texture.getWidth();
        else if (isMouseOver() || state != State.INACTIVE)
            iconU += texture.getWidth() * 2;

        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(0, 0, iconU, texture.getV(), texture);

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver() || tooltip.isEmpty()) return;
        drawToolTip(this, tooltip);
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            State prevState = state;
            switch (state) {
                case INACTIVE:
                case UP:
                    state = State.DOWN;
                    break;
                case DOWN:
                    state = State.UP;
                    break;
            }
            if (listener != null)
                listener.sort(prevState, state);
            return true;
        }
        return false;
    }

    @Nonnull
    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(@Nonnull String tooltip) {
        this.tooltip = tooltip;
    }

    @Nonnull
    public Texture getUpTexture() {
        return upTexture;
    }

    public Sorter setUpTexture(@Nonnull Texture texture) {
        upTexture = texture;
        return this;
    }

    @Nonnull
    public Texture getDownTexture() {
        return downTexture;
    }

    public Sorter setDownTexture(@Nonnull Texture texture) {
        downTexture = texture;
        return this;
    }

    @Override
    public String toString() {
        return "Sorter[" +
                "x= " + getX() + ", " +
                "y= " + getY() + ", " +
                "state= " + state + ", " +
                "upTexture= " + upTexture + ", " +
                "downTexture= " + downTexture + "" +
                "]";
    }

    public enum State {

        INACTIVE,
        UP,
        DOWN
    }

    @FunctionalInterface
    public interface StateChangeListener {

        void sort(@Nonnull State oldState, @Nonnull State newState);
    }
}
