package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import net.minecraft.client.renderer.GlStateManager;

public abstract class OxygenBackgroundFiller extends GUISimpleElement<OxygenBackgroundFiller> {

    private final boolean gradient;

    public OxygenBackgroundFiller(int xPosition, int yPosition, int width, int height) {             
        this.setPosition(xPosition, yPosition);         
        this.setSize(width, height);
        this.setStaticBackgroundColor(EnumBaseGUISetting.FILL_SCREEN_COLOR.get().asInt());
        this.setDynamicBackgroundColor(EnumBaseGUISetting.BACKGROUND_BASE_COLOR.get().asInt(), EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt(), 0);
        this.gradient = EnumBaseGUISetting.VERTICAL_GRADIENT.get().asBoolean();
    }

    @Override
    public void draw(int mouseX, int mouseY) {  
        GlStateManager.pushMatrix();            
        GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);   

        //fill
        drawRect(- this.screen.guiLeft, - this.screen.guiTop, this.mc.displayWidth, this.mc.displayHeight, this.getStaticBackgroundColor());

        //main background  
        if (this.gradient) {
            OxygenGUIUtils.drawGradientRect(0.0D, - this.screen.guiTop, this.getWidth(), 0.0D, 0x00000000, this.getEnabledBackgroundColor(), EnumGUIAlignment.BOTTOM);
            OxygenGUIUtils.drawGradientRect(0.0D, this.getHeight(), this.getWidth(), this.getHeight() + this.screen.guiTop, 0x00000000, this.getEnabledBackgroundColor(), EnumGUIAlignment.TOP);
        }

        this.drawBackground();

        GlStateManager.popMatrix();            
    }

    public abstract void drawBackground();
}
