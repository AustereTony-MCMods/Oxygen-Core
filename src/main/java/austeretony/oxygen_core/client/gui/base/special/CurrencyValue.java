package austeretony.oxygen_core.client.gui.base.special;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.Texts;
import austeretony.oxygen_core.client.gui.base.block.Text;
import austeretony.oxygen_core.client.gui.base.core.Widget;
import austeretony.oxygen_core.client.preset.CurrencyProperties;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.CommonUtils;

import javax.annotation.Nonnull;

public class CurrencyValue extends Widget<CurrencyValue> {

    @Nonnull
    protected Text text;
    @Nonnull
    protected SpecialState state;

    protected CurrencyProperties properties;
    protected long value;

    public CurrencyValue(int x, int y, @Nonnull Text text) {
        setPosition(x, y);
        setSize(8, 8);
        this.text = text;
        state = SpecialState.NORMAL;
        properties = OxygenClient.getCurrencyProperties(OxygenMain.CURRENCY_COINS);

        setEnabled(true);
        setVisible(true);
    }

    public CurrencyValue(int x, int y) {
        this(x , y, Texts.additional(""));
    }

    public CurrencyValue setCurrency(int index, long value) {
        properties = OxygenClient.getCurrencyProperties(index);
        this.value = value;
        text.setText(CommonUtils.formatCurrencyValue(value));
        return this;
    }

    public CurrencyValue setCurrency(int index) {
        properties = OxygenClient.getCurrencyProperties(index);
        return this;
    }

    public CurrencyValue setValue(long value) {
        this.value = value;
        text.setText(CommonUtils.formatCurrencyValue(value));
        return this;
    }

    public CurrencyValue setState(@Nonnull SpecialState state) {
        this.state = state;
        return this;
    }

    @Nonnull
    public CurrencyProperties getCurrencyProperties() {
        return properties;
    }

    public int getCurrencyIndex() {
        return properties.getCurrencyIndex();
    }

    public long getValue() {
        return value;
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.colorDef();
        int yOffset = properties != null ? properties.getIconYOffset() : 0;
        int width = properties != null ? properties.getIconWidth() : 0;
        int height = properties != null ? properties.getIconHeight() : 0;
        float yPos = ((float) getHeight() - height) / 2F + yOffset;
        GUIUtils.drawTexturedRect(properties.getIconXOffset(), yPos, width, height, properties.getIconTexture(), 0, 0, width, height);

        GUIUtils.pushMatrix();
        float x = -2F - GUIUtils.getTextWidth(text.getText(), text.getScale());
        float y = ((float) getHeight() - GUIUtils.getTextHeight(text.getScale())) / 2F + .5F;
        GUIUtils.translate(x, y);
        GUIUtils.scale(text.getScale(), text.getScale());
        GUIUtils.drawString(text.getText(), 0, 0, state.colorHex, text.isShadowEnabled());
        GUIUtils.popMatrix();

        GUIUtils.popMatrix();
    }

    @Override
    public void drawForeground(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible() || !isMouseOver()) return;
        drawToolTip(this, properties.getLocalizedName());
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
        return "CurrencyValue[x= " + getX() + ", y= " + getY()
                + ", currency index= " + properties.getCurrencyIndex() + ", value= " + value + "]";
    }
}
