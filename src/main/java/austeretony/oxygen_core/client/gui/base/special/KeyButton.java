package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.MouseButtons;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.settings.CoreSettings;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class KeyButton extends Widget<KeyButton> {

    protected int keyCode;
    protected String keyStr;
    @Nonnull
    protected Text text;

    @Nullable
    protected Runnable pressListener;

    public KeyButton(int x, int y, int keyCode, @Nonnull Text text) {
        setPosition(x, y);
        this.keyCode = keyCode;
        keyStr = "[" + GUIUtils.getKeyDisplayString(keyCode) + "]";
        this.text = text;
        setSize((int) Math.ceil(GUIUtils.getTextWidth(keyStr, this.text.getScale())), 8);

        setEnabled(true);
        setVisible(true);
    }

    public KeyButton(int x, int y, int keyCode, @Nonnull String text) {
        this(x, y, keyCode, Texts.button(text));
    }

    public KeyButton setPressListener(Runnable listener) {
        this.pressListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            GUIUtils.pushMatrix();
            GUIUtils.translate(getX(), getY());

            GUIUtils.pushMatrix();
            GUIUtils.translate(text.getOffset(), getHeight() - GUIUtils.getTextHeight(text.getScale()));
            GUIUtils.scale(text.getScale(), text.getScale());

            int color = text.getColorEnabled();
            if (!isEnabled())
                color = text.getColorDisabled();
            else if (isMouseOver())
                color = text.getColorMouseOver();

            GUIUtils.drawString(keyStr, 0, 0, color, text.isShadowEnabled());
            GUIUtils.drawString(text.getText(), getWidth() + 8, 0, isEnabled()
                            ? CoreSettings.COLOR_TEXT_ADDITIONAL_ENABLED.asInt()
                            : CoreSettings.COLOR_TEXT_ADDITIONAL_DISABLED.asInt(),
                    text.isShadowEnabled());
            GUIUtils.popMatrix();

            GUIUtils.popMatrix();
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY) && mouseButton == MouseButtons.LEFT_BUTTON) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            if (pressListener != null)
                pressListener.run();
            return true;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char typedChar, int keyCode) {
        if (!isEnabled()) return false;
        if (this.keyCode == keyCode) {
            MinecraftClient.playUISound(SoundEffects.uiButtonClick);
            if (pressListener != null)
                pressListener.run();
            return true;
        }
        return false;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public KeyButton setText(@Nonnull Text text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return "KeyButton[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", keyCode= " + keyCode + ", text= " + text.toString() + "]";
    }
}
