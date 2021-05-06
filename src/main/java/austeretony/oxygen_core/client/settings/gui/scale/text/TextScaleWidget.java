package austeretony.oxygen_core.client.settings.gui.scale.text;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.settings.CoreSettings;

public class TextScaleWidget extends Widget<TextScaleWidget> {

    protected float textScale;

    public TextScaleWidget(int x, int y, float textScale) {
        setPosition(x, y);
        setSize(30, 8);
        this.textScale = textScale;

        setVisible(true);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.pushMatrix();
        GUIUtils.translate(0F, getHeight() - GUIUtils.getTextHeight(textScale));
        GUIUtils.scale(textScale, textScale);

        String text = localize("oxygen_core.gui.settings.widget.test");
        GUIUtils.drawString(text, 0, 0, CoreSettings.COLOR_TEXT_ADDITIONAL_ENABLED.asInt(), false);
        GUIUtils.popMatrix();

        GUIUtils.popMatrix();
    }

    public float getTextScale() {
        return textScale;
    }

    public void setTextScale(float scale) {
        textScale = scale;
    }
}
