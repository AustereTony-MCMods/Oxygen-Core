package austeretony.oxygen_core.client.gui.base.button;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.MouseClickListener;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;
import net.minecraft.client.renderer.GlStateManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ImageButton extends Widget<ImageButton> {

    @Nonnull
    protected Texture texture;
    @Nonnull
    protected String tooltip;

    @Nullable
    protected MouseClickListener clickListener;

    public ImageButton(int x, int y, int width, int height, @Nonnull Texture texture, @Nonnull String tooltip) {
        setPosition(x, y);
        setSize(width, height);
        this.texture = texture;
        this.tooltip = tooltip;

        setEnabled(true);
        setVisible(true);
    }

    public ImageButton(int x, int y, int width, int height, @Nonnull Texture texture) {
        this(x, y, width, height, texture, "");
    }

    public ImageButton setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        int iconU = texture.getU();
        if (!isEnabled())
            iconU += texture.getWidth();
        else if (isMouseOver())
            iconU += texture.getWidth() * 2;
        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect((getWidth() - texture.getWidth()) / 2.0, (getHeight() - texture.getHeight()) / 2.0,
                iconU, texture.getV(), texture);

        GlStateManager.disableBlend();

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
        if (isMouseOver() && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            if (clickListener != null) {
                clickListener.click(mouseX, mouseY, mouseButton);
            }
            return true;
        }
        return false;
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(@Nonnull Texture texture) {
        this.texture = texture;
    }

    @Nonnull
    public String getTooltip() {
        return tooltip;
    }

    public void setTooltip(@Nonnull String tooltip) {
        this.tooltip = tooltip;
    }

    @Override
    public String toString() {
        return "ImageButton[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", texture= " + texture + ", tooltip= " + tooltip + "]";
    }
}
