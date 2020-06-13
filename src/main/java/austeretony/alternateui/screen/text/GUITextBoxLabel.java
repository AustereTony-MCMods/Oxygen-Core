package austeretony.alternateui.screen.text;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.alternateui.util.UIUtils;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author AustereTony
 */
public class GUITextBoxLabel extends GUISimpleElement<GUITextBoxLabel> {

    private final List<String> lines = new ArrayList<>(5);

    protected int lineOffset;

    public GUITextBoxLabel(int xPosition, int yPosition, int width, int height) {           
        this.setPosition(xPosition, yPosition);
        this.setSize(width, height);           
        this.lineOffset = 2;       
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        if (this.isVisible()) {
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            if (this.isDynamicBackgroundEnabled())                       
                this.drawRect(0, 0, this.getWidth(), this.getHeight(), this.getEnabledColor());  

            GlStateManager.pushMatrix();           
            GlStateManager.translate(1.0F, 2.0F, 0.0F);            
            GlStateManager.scale(this.getTextScale(), this.getTextScale(), 0.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

            int index = 0;
            for (String line : this.lines) {
                this.mc.fontRenderer.drawString(line, 0.0F, (this.mc.fontRenderer.FONT_HEIGHT + this.lineOffset) * index, this.getEnabledTextColor(), this.isTextShadowEnabled());
                index++;
            }

            GlStateManager.popMatrix();

            GlStateManager.popMatrix();
        }
    }

    @Override
    public GUITextBoxLabel setDisplayText(String displayText) {  
        UIUtils.divideText(this.lines, displayText, this.getWidth() - 4, this.getHeight(), this.getTextScale(), this.lineOffset);
        return this;
    }

    public GUITextBoxLabel setLineOffset(int offset) {
        this.lineOffset = offset;
        return this;
    }
}
