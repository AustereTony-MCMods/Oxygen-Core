package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TextFormattingColorPicker extends Widget<TextFormattingColorPicker> {

    private static final int[] HEX_COLORS = new int[16];

    static {
        int i, j, k, l, m;
        for (i = 0; i < 16; i++) {
            j = (i >> 3 & 1) * 85;
            k = (i >> 2 & 1) * 170 + j;
            l = (i >> 1 & 1) * 170 + j;
            m = (i >> 0 & 1) * 170 + j;
            if (i == 6)
                k += 85;
            HEX_COLORS[i] = 0xFF << 24 | (k & 0xFF) << 16 | (l & 0xFF) << 8 | m & 0xFF;
        }
    }

    private static final int
            SEGMENT_WIDTH = 6,
            SEGMENT_HEIGHT = 7;

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected TextFormatting formatting;

    @Nullable
    protected ColorPickListener pickListener;

    public TextFormattingColorPicker(int x, int y, @Nonnull TextFormatting formatting) {
        setPosition(x, y);
        setSize(SEGMENT_WIDTH * HEX_COLORS.length, SEGMENT_HEIGHT);
        fill = Fills.button();
        this.formatting = formatting;

        setEnabled(true);
        setVisible(true);
    }

    public TextFormattingColorPicker setColorPickListener(ColorPickListener listener) {
        pickListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        for (int i = 0; i < HEX_COLORS.length; i++)
            GUIUtils.drawRect(i * SEGMENT_WIDTH, 0, i * SEGMENT_WIDTH + SEGMENT_WIDTH, SEGMENT_HEIGHT,
                    HEX_COLORS[i]);

        int
                index = formatting.ordinal(),
                frameX = SEGMENT_WIDTH * index;
        GUIUtils.drawFrame(frameX, 0, SEGMENT_WIDTH, SEGMENT_HEIGHT, 1,
                fill.getColorEnabled());

        if (isMouseOver()) {
            int mouseOverX = (mouseX - getX()) / SEGMENT_WIDTH;
            mouseOverX *= SEGMENT_WIDTH;
            GUIUtils.colorDef();
            GUIUtils.drawFrame(mouseOverX, 0, SEGMENT_WIDTH, SEGMENT_HEIGHT, 1,
                    fill.getColorMouseOver());
        }

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver()) return;
        drawToolTip(this, formatting + formatting.name());
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            TextFormatting from = formatting;
            int index = (adjustMouseX(mouseX) - getX()) / SEGMENT_WIDTH;
            formatting = TextFormatting.fromColorIndex(index);
            if (pickListener != null)
                pickListener.pick(from, formatting);
            return true;
        }
        return false;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public TextFormattingColorPicker setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return this;
    }

    @Nonnull
    public TextFormatting getFormatting() {
        return formatting;
    }

    public void setFormatting(@Nonnull TextFormatting formatting) {
        this.formatting = formatting;
    }

    @Override
    public String toString() {
        return "TextFormattingColorPicker[x= " + getX() + ", y= " + getY() + ", formatting= " + formatting.name() + "]";
    }

    @FunctionalInterface
    public interface ColorPickListener {

        void pick(@Nonnull TextFormatting from, @Nonnull TextFormatting to);
    }
}
