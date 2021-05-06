package austeretony.oxygen_core.client.gui.base.block;

import austeretony.oxygen_core.client.gui.base.Alignment;
import austeretony.oxygen_core.client.gui.base.GUIUtils;

import javax.annotation.Nonnull;

public class Text {

    protected String text = "";
    protected float scale;
    protected Alignment alignment = Alignment.RIGHT;
    protected int colorDefault, colorEnabled, colorDisabled, colorMouseOver, offset;
    protected boolean shadowEnabled;

    private Text() {}

    public static Builder builder() {
        return new Text().new Builder();
    }

    @Nonnull
    public String getText() {
        return text;
    }

    public Text setText(@Nonnull String text) {
        this.text = text;
        return this;
    }

    public float getScale() {
        return scale;
    }

    public Text setScale(float scale) {
        this.scale = scale;
        return this;
    }

    public Text incrementScale(float increment) {
        scale += increment;
        return this;
    }

    public Text decrementScale(float decrement) {
        scale -= decrement;
        return this;
    }

    public int getColorDefault() {
        return colorDefault;
    }

    public Text setColorDefault(int color) {
        colorDefault = color;
        return this;
    }

    public int getColorEnabled() {
        return colorEnabled;
    }

    public Text setColorEnabled(int color) {
        colorEnabled = color;
        return this;
    }

    public int getColorDisabled() {
        return colorDisabled;
    }

    public Text setColorDisabled(int color) {
        colorDisabled = color;
        return this;
    }

    public int getColorMouseOver() {
        return colorMouseOver;
    }

    public Text setColorMouseOver(int color) {
        colorMouseOver = color;
        return this;
    }

    @Nonnull
    public Alignment getAlignment() {
        return alignment;
    }

    public Text setAlignment(@Nonnull Alignment alignment) {
        this.alignment = alignment;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public Text setOffset(int offset) {
        this.offset = offset;
        return this;
    }

    public boolean isShadowEnabled() {
        return shadowEnabled;
    }

    public Text setShadowEnabled(boolean flag) {
        this.shadowEnabled = flag;
        return this;
    }

    public float getWidth() {
        return GUIUtils.getTextWidth(text, scale);
    }

    public float getWidth(float scale) {
        return GUIUtils.getTextWidth(text, scale);
    }

    public float getHeight() {
        return GUIUtils.getTextHeight(scale);
    }

    public float getHeight(float scale) {
        return GUIUtils.getTextHeight(scale);
    }

    @Override
    public String toString() {
        return "Text[text= " + text + ", scale= " + scale + ", alignment= " + alignment + ", colorDefault= " + Integer.toHexString(colorDefault)
                + ", colorEnabled= " + Integer.toHexString(colorEnabled) + ", colorDisabled= " + Integer.toHexString(colorDisabled)
                + ", colorMouseOver= " + Integer.toHexString(colorMouseOver) + ", offset= " + offset + ", shadow= " + shadowEnabled + "]";
    }

    public class Builder {

        private Builder() {}

        public Builder text(String text) {
            Text.this.text = text;
            return this;
        }

        public Builder scale(float factor) {
            Text.this.scale = factor;
            return this;
        }

        public Builder color(int def, int enabled, int disabled, int hovered) {
            Text.this.colorDefault = def;
            Text.this.colorEnabled = enabled;
            Text.this.colorDisabled = disabled;
            Text.this.colorMouseOver = hovered;
            return this;
        }

        public Builder color(int enabled, int disabled, int hovered) {
            Text.this.colorDefault = enabled;
            return color(enabled, disabled, hovered);
        }

        public Builder colorDefault(int color) {
            Text.this.colorDefault = color;
            return this;
        }

        public Builder colorEnabled(int color) {
            Text.this.colorEnabled = color;
            return this;
        }

        public Builder colorDisabled(int color) {
            Text.this.colorDisabled = color;
            return this;
        }

        public Builder colorHovered(int color) {
            Text.this.colorMouseOver = color;
            return this;
        }

        public Builder alignment(Alignment alignment, int offset) {
            Text.this.alignment = alignment;
            Text.this.offset = offset;
            return this;
        }

        public Builder alignment(Alignment alignment) {
            Text.this.alignment = alignment;
            return this;
        }

        public Builder offset(int value) {
            Text.this.offset = value;
            return this;
        }

        public Builder shadow(boolean flag) {
            Text.this.shadowEnabled = flag;
            return this;
        }

        public Text build() {
            return Text.this;
        }
    }
}
