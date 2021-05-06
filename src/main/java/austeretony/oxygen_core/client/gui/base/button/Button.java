package austeretony.oxygen_core.client.gui.base.button;

import austeretony.oxygen_core.client.gui.base.*;
import austeretony.oxygen_core.client.gui.base.block.Fill;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.gui.base.listener.MouseClickListener;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.sound.SoundEffects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Button extends Widget<Button> {

    @Nonnull
    protected Fill fill;
    @Nonnull
    protected Text text;

    @Nullable
    protected MouseClickListener clickListener;

    public Button(int x, int y, int width, int height, @Nonnull Text text) {
        setPosition(x, y);
        setSize(width, height);
        fill = Fills.element();
        this.text = text;

        setEnabled(true);
        setVisible(true);
    }

    public Button(int x, int y, int width, int height, @Nonnull String text) {
        this(x, y, width, height, Texts.button(text));
    }

    public Button(int x, int y, int width, int height) {
        this(x, y, width, height, "");
    }

    public Button setMouseClickListener(MouseClickListener listener) {
        clickListener = listener;
        return this;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.drawRect(0, 0, getWidth(), getHeight(), getColorFromState(fill));
        GUIUtils.drawFrame(0, 0, getWidth(), getHeight());

        float textOffset = text.getAlignment() == Alignment.RIGHT ? -text.getOffset()
                : (text.getAlignment() == Alignment.LEFT ? text.getOffset()
                : (getWidth() - GUIUtils.getTextWidth(text.getText(), text.getScale())) / 2);
        GUIUtils.pushMatrix();
        GUIUtils.translate(textOffset,(getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2 + .5F);
        GUIUtils.scale(text.getScale(), text.getScale());

        GUIUtils.drawString(text.getText(), 0, 0, getColorFromState(text), text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.popMatrix();
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (!isEnabled()) return false;
        if (mouseOver(mouseX, mouseY)) {
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

    public Button setFill(@Nonnull Fill fill) {
        this.fill = fill;
        return this;
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public Button setText(@Nonnull Text text) {
        this.text = text;
        return this;
    }

    @Override
    public String toString() {
        return "Button[x= " + getX() + ", y= " + getY() + ", width= " + getWidth() + ", height= " + getHeight()
                + ", fill= " + fill + ", text= " + text + "]";
    }
}
