package austeretony.oxygen_core.client.gui.base.block;

public class Fill {

    protected int colorDefault, colorEnabled, colorDisabled, colorMouseOver;

    private Fill() {}

    public static Builder builder() {
        return new Fill().new Builder();
    }

    public int getColorDefault() {
        return colorDefault;
    }

    public void setColorDefault(int color) {
        colorDefault = color;
    }

    public int getColorEnabled() {
        return colorEnabled;
    }

    public void setColorEnabled(int color) {
        colorEnabled = color;
    }

    public int getColorDisabled() {
        return colorDisabled;
    }

    public void setColorDisabled(int color) {
        colorDisabled = color;
    }

    public int getColorMouseOver() {
        return colorMouseOver;
    }

    public void setColorMouseOver(int color) {
        colorMouseOver = color;
    }

    @Override
    public String toString() {
        return "Fill[colorDefault= " + Integer.toHexString(colorDefault) + ", colorEnabled= " + Integer.toHexString(colorEnabled)
                + ", colorDisabled= " + Integer.toHexString(colorDisabled) + ", colorMouseOver= " + Integer.toHexString(colorMouseOver) + "]";
    }

    public class Builder {

        private Builder() {}

        public Builder color(int def, int enabled, int disabled, int hovered) {
            Fill.this.colorDefault = def;
            Fill.this.colorEnabled = enabled;
            Fill.this.colorDisabled = disabled;
            Fill.this.colorMouseOver = hovered;
            return this;
        }

        public Builder color(int enabled, int disabled, int hovered) {
            Fill.this.colorDefault = enabled;
            return color(enabled, disabled, hovered);
        }

        public Builder colorDefault(int color) {
            Fill.this.colorDefault = color;
            return this;
        }

        public Builder colorEnabled(int color) {
            Fill.this.colorEnabled = color;
            return this;
        }

        public Builder colorDisabled(int color) {
            Fill.this.colorDisabled = color;
            return this;
        }

        public Builder colorHovered(int color) {
            Fill.this.colorMouseOver = color;
            return this;
        }

        public Fill build() {
            return Fill.this;
        }
    }
}
