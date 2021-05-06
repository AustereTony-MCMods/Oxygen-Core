package austeretony.oxygen_core.client.settings.gui.scale.text;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.MouseClickListener;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.settings.SettingValue;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TextScaleButton extends Widget<TextScaleButton> {

    @Nonnull
    protected final SettingValue scaleSetting;

    @Nullable
    protected MouseClickListener clickListener;

    public TextScaleButton(int x, int y, @Nonnull SettingValue scaleSetting) {
        setPosition(x, y);
        setSize(24, 6);
        this.scaleSetting = scaleSetting;

        setEnabled(true);
        setVisible(true);
    }

    public TextScaleButton setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.pushMatrix();
        float scale = CoreSettings.SCALE_TEXT_ADDITIONAL.asFloat();
        GUIUtils.translate(0F, getHeight() - GUIUtils.getTextHeight(scale));
        GUIUtils.scale(scale, scale);

        int color = CoreSettings.COLOR_TEXT_BASE_ENABLED.asInt();
        if (!isEnabled())
            color = CoreSettings.COLOR_TEXT_BASE_DISABLED.asInt();
        else if (isMouseOver())
            color = CoreSettings.COLOR_TEXT_BASE_MOUSE_OVER.asInt();
        String text = String.format("%.1f", GUIUtils.getTextHeight(scaleSetting.asFloat()))
                + " " + localize("oxygen_core.gui.settings.widget.pixels");
        GUIUtils.drawString(text, 0, 0, color, false);
        GUIUtils.popMatrix();

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            if (clickListener != null) {
                clickListener.click(mouseX, mouseY, mouseButton);
            }
            return true;
        }
        return false;
    }
}
