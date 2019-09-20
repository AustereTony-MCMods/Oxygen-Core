package austeretony.alternateui.screen.text;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.renderer.GlStateManager;

/**
 * @author AustereTony
 */
public class GUITextBoxLabel extends GUISimpleElement<GUITextBoxLabel> {

    private final List<String> lines = new ArrayList<String>(5);

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

            int index = 0;//fix for equal lines render issue (if List#indexOf() used)
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
        this.lines.clear(); 
        int width = this.getWidth() - 3;
        StringBuilder builder = new StringBuilder();    
        int 
        index = 0, 
        wordStartIndex = 0;
        boolean
        rechedLimit = false,
        wordProcessing = false;
        char prevSymbol = '0';
        String line;
        for (char symbol : displayText.toCharArray()) {
            if ((this.textHeight(this.getTextScale()) + this.lineOffset) * this.lines.size() >= this.getHeight())
                break;
            if (symbol != ' ') {
                wordProcessing = true;
                if (prevSymbol == ' ')
                    wordStartIndex = index;
            }
            if (symbol == '\n') {
                this.lines.add(builder.toString());
                builder.delete(0, builder.length());
                index = 0;
                continue;
            }
            if (this.textWidth(builder.toString() + String.valueOf(symbol), this.getTextScale()) <= width)
                builder.append(symbol);
            else {
                if (symbol == '.' 
                        || symbol == ',' 
                        || symbol == '!'
                        || symbol == '?')
                    builder.append(symbol);
                if (wordProcessing) {
                    this.lines.add(builder.toString().substring(0, wordStartIndex));
                    builder.delete(0, wordStartIndex);
                } else {
                    this.lines.add(builder.toString());
                    builder.delete(0, builder.length());
                }
                if (symbol != ' ')
                    builder.append(symbol);
                index = builder.length() - 1;
            }
            wordProcessing = false;
            prevSymbol = symbol;
            index++;
        }
        if (builder.length() != 0)
            this.lines.add(builder.toString());
        return this;
    }

    public GUITextBoxLabel setLineOffset(int offset) {
        this.lineOffset = offset;
        return this;
    }
}
