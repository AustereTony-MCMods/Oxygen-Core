package austeretony.alternateui.screen.text;

import java.util.ArrayList;
import java.util.List;

import austeretony.alternateui.screen.core.GUISimpleElement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Графический элемент в виде набора строк.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUITextBoxLabel extends GUISimpleElement<GUITextBoxLabel> {

    private final List<String> lines = new ArrayList<String>();

    private int lineDistance;

    public GUITextBoxLabel(int xPosition, int yPosition, int width, int height) {		
        this.setPosition(xPosition, yPosition);
        this.setSize(width, height);	       
        this.lineDistance = 2;       
        this.enableFull();
    }

    @Override
    public void draw(int mouseX, int mouseY) {  	
        if (this.isVisible()) {        	
            GlStateManager.pushMatrix();            
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);            
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);     	
            if (this.isDynamicBackgroundEnabled())                       
                this.drawRect(ZERO, ZERO, this.getWidth(), this.getHeight(), this.getEnabledColor());         
            if (!this.lines.isEmpty())       		
                for (String line : this.lines)       		
                    this.mc.fontRenderer.drawString(line, ZERO, (8 + this.getLineDistance()) * this.lines.indexOf(line), this.getEnabledTextColor(), this.isTextShadowEnabled());      	
            GlStateManager.popMatrix();
        }
    }

    public int getLineDistance() {   	
        return this.lineDistance;
    }

    public GUITextBoxLabel setLineDistance(int distance) {   	
        this.lineDistance = distance;   	
        return this;
    }

    /**
     * Установка текста, отображаемого элементом. 
     * 
     * @param displayText
     * 
     * @return вызывающий объект
     */
    @Override
    public GUITextBoxLabel setDisplayText(String displayText) {  	
        this.lines.clear();   	
        StringBuilder stringBuilder = new StringBuilder();    	
        String[] words = displayText.split("[ ]");    	
        if (words.length > 0) {   		
            for (int i = 0; i < words.length; i++) {  		
                if (this.width(stringBuilder.toString() + words[i]) < this.getWidth())	    			
                    stringBuilder.append(words[i]).append(" ");
                else {	    			
                    if (this.lines.size() * 10 < this.getHeight())	    				
                        this.lines.add(stringBuilder.toString());			
                    stringBuilder = new StringBuilder();	    			
                    stringBuilder.append(words[i]).append(" ");
                }	    		
                if (i == words.length - 1)	    			
                    if (this.lines.size() * (8 + this.getLineDistance()) < this.getHeight())	    			
                        this.lines.add(stringBuilder.toString());
            }
        }   	
        return this;
    }
}
