package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;
import austeretony.oxygen_core.client.gui.OxygenGUIUtils;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OxygenTexturedButton extends GUISimpleElement<OxygenTexturedButton> {

    private ClickListener clickListener;

    private final ResourceLocation texture;

    private final int textureWidth, textureHeight;

    public OxygenTexturedButton(int xPosition, int yPosition, int buttonWidth, int buttonHeight, ResourceLocation texture, int textureWidth, int textureHeight, String tooltip) {
        this.setPosition(xPosition, yPosition);
        this.setSize(buttonWidth, buttonHeight);
        this.texture = texture;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.setSound(OxygenSoundEffects.BUTTON_CLICK.soundEvent);
        this.setStaticBackgroundColor(EnumBaseGUISetting.BACKGROUND_ADDITIONAL_COLOR.get().asInt());
        if (!tooltip.isEmpty())
            this.initTooltip(tooltip, EnumBaseGUISetting.TOOLTIP_TEXT_COLOR.get().asInt(), EnumBaseGUISetting.TOOLTIP_BACKGROUND_COLOR.get().asInt(), EnumBaseGUISetting.TEXT_TOOLTIP_SCALE.get().asFloat());
        this.enableFull();
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int iconU = 0;
            if (!this.isEnabled())
                iconU = this.textureWidth;
            else if (this.isHovered() || this.isToggled())
                iconU = this.textureWidth * 2;

            GlStateManager.enableBlend(); 
            this.mc.getTextureManager().bindTexture(this.texture);
            GUIAdvancedElement.drawCustomSizedTexturedRect((this.getWidth() - this.textureWidth) / 2, (this.getHeight() - this.textureHeight) / 2, 
                    iconU, 0, this.textureWidth, this.textureHeight, this.textureWidth * 3, this.textureHeight);          
            GlStateManager.disableBlend(); 

            GlStateManager.popMatrix();       
        }
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {   
        if (this.isVisible() && this.isHovered() && this.hasTooltip()) {
            int 
            width = this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor()) + 6,
            height = 8;
            GlStateManager.pushMatrix();           
            GlStateManager.translate((this.getX() + this.getWidth() / 2) - (width / 2), this.getY() - height - 2, 0.0F);            
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            //background
            drawRect(0, 0, width, height, this.getTooltipBackgroundColor());

            //frame
            OxygenGUIUtils.drawRect(0.0D, 0.0D, 0.4D, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect((double) width - 0.4D, 0.0D, (double) width, (double) height, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, 0.0D, (double) width, 0.4D, this.getStaticBackgroundColor());
            OxygenGUIUtils.drawRect(0.0D, (double) height - 0.4D, (double) width, (double) height, this.getStaticBackgroundColor());

            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            GlStateManager.pushMatrix();           
            GlStateManager.translate((width - this.textWidth(this.getTooltipText(), this.getTooltipScaleFactor())) / 2, (height - this.textHeight(this.getTooltipScaleFactor())) / 2 + 1, 0.0F);            
            GlStateManager.scale(this.getTooltipScaleFactor(), this.getTooltipScaleFactor(), 0.0F);

            this.mc.fontRenderer.drawString(this.getTooltipText(), 0, 0, this.getTooltipTextColor(), false);

            GlStateManager.popMatrix();      

            GlStateManager.popMatrix();          
        }
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {      
        boolean flag = super.mouseClicked(mouseX, mouseY, mouseButton);         
        if (flag) {      
            if (this.clickListener != null)
                this.clickListener.onClick(mouseX, mouseY, mouseButton);
            this.screen.handleElementClick(this.screen.getWorkspace().getCurrentSection(), this);               
            this.screen.getWorkspace().getCurrentSection().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);                                               
            if (this.screen.getWorkspace().getCurrentSection().hasCurrentCallback())                    
                this.screen.getWorkspace().getCurrentSection().getCurrentCallback().handleElementClick(this.screen.getWorkspace().getCurrentSection(), this, mouseButton);
        }                               
        return flag;
    }

    public static interface ClickListener {

        void onClick(int mouseX, int mouseY, int mouseButton);
    }
}