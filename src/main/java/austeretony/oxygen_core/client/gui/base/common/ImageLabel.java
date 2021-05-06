package austeretony.oxygen_core.client.gui.base.common;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.core.Widget;

import javax.annotation.Nonnull;

public class ImageLabel extends Widget<ImageLabel> {

    @Nonnull
    protected Texture texture;

    public ImageLabel(int x, int y, @Nonnull Texture texture) {
        setPosition(x, y);
        this.texture = texture;

        setVisible(true);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(0, 0, texture.getWidth(), texture.getHeight(), texture.getTexture(), texture.getU(),
                texture.getV(), texture.getImageWidth(), texture.getImageHeight());

        GUIUtils.popMatrix();
    }

    @Nonnull
    public Texture getTexture() {
        return texture;
    }

    public void setTexture(@Nonnull Texture texture) {
        this.texture = texture;
    }

    @Override
    public String toString() {
        return "ImageLabel[x= " + getX() + ", y= " + getY() + ", texture= " + texture + "]";
    }
}
