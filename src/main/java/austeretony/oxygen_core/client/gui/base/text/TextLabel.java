package austeretony.oxygen_core.client.gui.base.text;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;

import javax.annotation.Nonnull;

public class TextLabel extends Widget<TextLabel> {

    @Nonnull
    protected Text text;

    public TextLabel(int x, int y, @Nonnull Text text) {
        setPosition(x, y);
        setText(text);

        setEnabled(true);
        setVisible(true);
    }

    public TextLabel(int x, int y) {
        this(x, y, Texts.def());
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (isVisible()) {
            GUIUtils.pushMatrix();
            GUIUtils.translate(getX(), getY());

            float textOffset = text.getAlignment() == Alignment.RIGHT ? 0 : -text.getWidth() - text.getOffset();
            GUIUtils.pushMatrix();
            GUIUtils.translate(textOffset, -GUIUtils.getTextHeight(text.getScale()));
            GUIUtils.scale(text.getScale(), text.getScale());
            GUIUtils.drawString(text.getText(), 0, 0, isEnabled() ? text.getColorEnabled() : text.getColorDisabled(),
                    text.isShadowEnabled());
            GUIUtils.popMatrix();

            GUIUtils.popMatrix();
        }
    }

    @Override
    public boolean mouseOver(int mouseX, int mouseY) {
        return false;
    }

    @Override
    public int getWidth() {
        return (int) Math.ceil(GUIUtils.getTextWidth(text.getText(), text.getScale()));
    }

    @Override
    public int getHeight() {
        return (int) Math.ceil(GUIUtils.getTextHeight(text.getScale()));
    }

    @Nonnull
    public Text getText() {
        return text;
    }

    public void setText(@Nonnull Text text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "TextLabel[x= " + getX() + ", y= " + getY() + ", text=" + text.toString() + "]";
    }
}
