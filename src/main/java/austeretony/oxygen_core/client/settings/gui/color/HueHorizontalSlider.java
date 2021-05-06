package austeretony.oxygen_core.client.settings.gui.color;

import austeretony.oxygen_core.client.gui.base.GUIUtils;
import austeretony.oxygen_core.client.gui.base.block.Texture;
import austeretony.oxygen_core.client.gui.base.button.HorizontalSlider;
import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public class HueHorizontalSlider extends HorizontalSlider {

    protected static final ResourceLocation HUE_RAMP = new ResourceLocation(OxygenMain.MOD_ID, "textures/gui/hue_ramp.png");

    @Nonnull
    protected Texture texture;

    public HueHorizontalSlider(int x, int y, int width, float initialHue) {
        super(x, y, width, initialHue);
        setSize(width, 6);
        texture = Texture.builder()
                .texture(HUE_RAMP)
                .size(128, 2)
                .imageSize(128, 2)
                .build();
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks) {
        if (!isVisible()) return;
        GUIUtils.pushMatrix();
        GUIUtils.translate(getX(), getY());

        int barY = (int) ((getHeight() - BAR_HEIGHT) / 2F);
        GUIUtils.colorDef();
        GUIUtils.drawTexturedRect(0, barY,
                texture.getWidth(), texture.getHeight(), texture.getTexture(), 0, texture.getV(),
                texture.getImageWidth(), texture.getImageHeight());
        GUIUtils.drawFrame(0, barY, getWidth(), BAR_HEIGHT);

        float carriageX = getWidth() * value - ((float) CARRIAGE_WIDTH / 2F);
        //int carriageColor = Color.HSBtoRGB(getValue(), 1F, 1F);
        GUIUtils.drawRect(carriageX, 0, carriageX + CARRIAGE_WIDTH, getHeight(),
                getColorFromState(getCarriageFill()));
        //Reference.drawFrame(carriageX, 0, CARRIAGE_WIDTH, getHeight());

        GUIUtils.popMatrix();
    }
}
