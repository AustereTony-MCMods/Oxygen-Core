package austeretony.oxygen_core.client.gui.base.button;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.Textures;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class CheckBox extends Widget<CheckBox> {

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected State state;
    @Nonnull
    protected Texture texture;

    @Nullable
    protected StateChangeListener stateChangeListener;

    public CheckBox(int x, int y, @Nonnull State state) {
        setPosition(x, y);
        setSize(6, 6);
        fill = Fills.button();
        this.state = state;
        texture = Texture.builder()
                .texture(Textures.CHECK_ICONS)
                .size(6, 6)
                .imageSize(18, 6)
                .build();

        setEnabled(true);
        setVisible(true);
    }

    public CheckBox(int x, int y) {
        this(x, y, State.INACTIVE);
    }

    public CheckBox setStateChangeListener(StateChangeListener listener) {
        stateChangeListener = listener;
        return this;
    }

    @Nonnull
    public State getState() {
        return state;
    }

    public CheckBox setState(@Nonnull State state) {
        this.state = state;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), CoreSettings.COLOR_BACKGROUND_BASE.asInt());
        int
                color = fill.getColorEnabled(),
                iconU = texture.getU();
        if (!isEnabled()) {
            color = fill.getColorDisabled();
            iconU += texture.getWidth();
        } else if (isMouseOver()) {
            color = fill.getColorMouseOver();
            iconU += texture.getWidth() * 2;
        }
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight(), FRAME_WIDTH, color);

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        if (state == State.ACTIVE) {
            GUIUtils.colorDef();
            GUIUtils.drawTexturedRect(0, 0, texture.getWidth(), texture.getHeight(), texture.getTexture(),
                    iconU, texture.getV(), texture.getImageWidth(), texture.getImageHeight());
        }

        GlStateManager.disableBlend();

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            state = state == State.ACTIVE ? State.INACTIVE : State.ACTIVE;
            if (stateChangeListener != null)
                stateChangeListener.click(state);
            return true;
        }
        return false;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public CheckBox setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return this;
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }

    public CheckBox setTexture(@Nonnull Texture texture) {
        this.texture = texture;
        return this;
    }

    @Override
    public String toString() {
        return "CheckBox[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", state= " + state + ", fill= " + fill + ", texture= " + texture + "]";
    }

    @FunctionalInterface
    public interface StateChangeListener {

        void click(@Nonnull State newState);
    }

    public enum State {

        ACTIVE,
        INACTIVE
    }
}
