package austeretony.oxygen_core.client.gui.base.block;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class Texture {

    @Nonnull
    protected ResourceLocation texture = GUIUtils.getMissingTextureLocation();
    protected int width, height, u, v, imageWidth, imageHeight;

    private Texture() {}

    public static Builder builder() {
        return new Texture().new Builder();
    }

    @Nonnull
    public ResourceLocation getTexture() {
        return texture;
    }

    public Texture setTexture(@Nonnull ResourceLocation texture) {
        this.texture = texture;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public Texture setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }

    public int getU() {
        return u;
    }

    public int getV() {
        return v;
    }

    public Texture setUV(int u, int v) {
        this.u = u;
        this.v = v;
        return this;
    }

    public int getImageWidth() {
        return imageWidth;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public Texture setImageSize(int width, int height) {
        imageWidth = width;
        imageHeight = height;
        return this;
    }

    @Override
    public String toString() {
        return "Texture[texture= " + texture + ", width= " + width + ", height= " + height + ", u= " + u + ", v= " + v
                + ", imageWidth= " + imageWidth + ", imageHeight= " + imageHeight + "]";
    }

    public class Builder {

        private Builder() {}

        public Builder texture(@Nonnull ResourceLocation texture) {
            Texture.this.texture = texture;
            return this;
        }

        public Builder size(int width, int height) {
            Texture.this.width = width;
            Texture.this.height = height;
            return this;
        }

        public Builder uv(int u, int v) {
            Texture.this.u = u;
            Texture.this.v = v;
            return this;
        }

        public Builder imageSize(int width, int height) {
            Texture.this.imageWidth = width;
            Texture.this.imageHeight = height;
            return this;
        }

        public Texture build() {
            return Texture.this;
        }
    }
}
