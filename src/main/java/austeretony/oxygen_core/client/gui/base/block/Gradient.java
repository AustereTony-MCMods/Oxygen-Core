package austeretony.oxygen_core.client.gui.base.block;

import austeretony.oxygen_core.client.gui.base.Alignment;

import javax.annotation.Nonnull;

public class Gradient {

    @Nonnull
    protected Alignment alignment = Alignment.LEFT;
    protected int colorFirst, colorSecond;

    private Gradient() {}

    public static Builder builder() {
        return new Gradient().new Builder();
    }

    public @Nonnull Alignment getAlignment() {
        return alignment;
    }

    public void setAlignment(@Nonnull Alignment alignment) {
        this.alignment = alignment;
    }

    public int getColorFirst() {
        return colorFirst;
    }

    public int getColorSecond() {
        return colorSecond;
    }

    public void setColor(int colorFirst, int colorSecond) {
        this.colorFirst = colorFirst;
        this.colorSecond = colorSecond;
    }

    @Override
    public String toString() {
        return "Gradient[alignment= " + alignment + ", colorFirst= " + Integer.toHexString(colorFirst)
                + ", colorSecond= " + Integer.toHexString(colorSecond) + "]";
    }

    public class Builder {

        private Builder() {}

        public Builder alignment(Alignment alignment) {
            Gradient.this.alignment = alignment;
            return this;
        }

        public Builder color(int colorFirst, int colorSecond) {
            Gradient.this.colorFirst = colorFirst;
            Gradient.this.colorSecond = colorSecond;
            return this;
        }

        public Gradient build() {
            return Gradient.this;
        }
    }
}
