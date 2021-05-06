package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.gui.base.Fills;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.MouseClickListener;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ColorButton extends Widget<ColorButton> {

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected final SettingValue colorSetting;

    @Nullable
    protected MouseClickListener clickListener;

    public ColorButton(int x, int y, @Nonnull SettingValue colorSetting) {
        setPosition(x, y);
        setSize(6, 6);
        fill = Fills.button();
        this.colorSetting = colorSetting;

        setEnabled(true);
        setVisible(true);
    }

    public ColorButton setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), colorSetting.asInt());
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight(), FRAME_WIDTH, getColorFromState(fill));

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            if (clickListener != null)
                clickListener.click(mouseX, mouseY, mouseButton);
            return true;
        }
        return false;
    }

    @Nonnull
    public Fill getFill() {
        return fill;
    }

    public ColorButton setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return this;
    }
}
