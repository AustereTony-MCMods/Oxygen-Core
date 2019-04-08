package austeretony.alternateui.overlay.core;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.alternateui.screen.core.GUIBaseElement;
import austeretony.alternateui.util.EnumGUIAlignment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GUIOverlay {

    private final Set<GUIBaseElement> elements = new LinkedHashSet<GUIBaseElement>();

    protected final Minecraft mc = Minecraft.getMinecraft();

    private EnumGUIAlignment alignment;

    private boolean isVisible;

    private int xPosition, yPosition, xOffset, yOffset, prevGuiScale;

    private float scaleFactor = 1.0F;

    public GUIOverlay(EnumGUIAlignment alignment, int xOffset, int yOffset, float scaleFactor) {
        this.setScale(scaleFactor);
        this.setAlignment(alignment, xOffset, yOffset);
        this.setVisible(true);
    }

    public GUIOverlay(EnumGUIAlignment alignment, float scaleFactor) {
        this(alignment, 0, 0, scaleFactor);
    }

    public GUIOverlay(float scaleFactor) {
        this(EnumGUIAlignment.CENTER, scaleFactor);
    }

    public void draw() {  
        if (this.mc.gameSettings.guiScale != this.prevGuiScale)
            this.setAlignment(this.alignment, this.xOffset, this.yOffset);
        if (this.isVisible()) {         
            GlStateManager.pushMatrix();           
            GlStateManager.translate(this.getX(), this.getY(), 0.0F);           
            GlStateManager.scale(this.getScale(), this.getScale(), 0.0F);    
            for (GUIBaseElement element : this.getElements())                  
                element.draw(0, 0);       
            GlStateManager.popMatrix();
        }
        this.prevGuiScale = this.mc.gameSettings.guiScale;
    }

    public Set<GUIBaseElement> getElements() {      
        return this.elements;
    }

    public GUIOverlay addElement(GUIBaseElement element) {              
        this.elements.add(element);     
        return this;
    }

    public GUIOverlay setAlignment(EnumGUIAlignment alignment, int xOffset, int yOffset) {     
        ScaledResolution scaledResolution = new ScaledResolution(this.mc);      
        this.alignment = alignment;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        int 
        xPos = 0, 
        yPos = 0;
        switch (alignment) {
        case BOTTOM:
            xPos = scaledResolution.getScaledWidth() / 2;
            yPos = scaledResolution.getScaledHeight();
            break;
        case BOTTOM_LEFT:
            xPos = 0;
            yPos = scaledResolution.getScaledHeight();
            break;
        case BOTTOM_RIGHT:
            xPos = 0;
            yPos = scaledResolution.getScaledHeight();
            break;
        case CENTER:
            xPos = scaledResolution.getScaledWidth() / 2;
            yPos = scaledResolution.getScaledHeight() / 2;
            break;
        case LEFT:
            xPos = 0;
            yPos = scaledResolution.getScaledHeight() / 2;
            break;
        case RIGHT:
            xPos = scaledResolution.getScaledWidth();
            yPos = scaledResolution.getScaledHeight() / 2;
            break;
        case TOP:
            xPos = scaledResolution.getScaledWidth() / 2;
            yPos = 0;
            break;
        case TOP_LEFT:
            xPos = 0;
            yPos = 0;
            break;
        case TOP_RIGHT:
            xPos = scaledResolution.getScaledWidth();
            yPos = 0;
            break;
        }
        this.setPosition(xPos + xOffset, yPos + yOffset);           
        return this;
    }

    public boolean isVisible() {    
        return this.isVisible;
    }

    public GUIOverlay setVisible(boolean isVisible) {        
        this.isVisible = isVisible;     
        return this;
    }

    public int getX() {     
        return this.xPosition;
    }

    public GUIOverlay setX(int xPosition) {      
        this.xPosition = xPosition;     
        return this;
    } 

    public int getY() {         
        return this.yPosition;
    }

    public GUIOverlay setY(int yPosition) {      
        this.yPosition = yPosition;     
        return this;
    } 

    public GUIOverlay setPosition(int xPosition, int yPosition) {    
        this.xPosition = xPosition;
        this.yPosition = yPosition;     
        return this;
    } 

    public float getScale() {
        return this.scaleFactor;
    }

    public GUIOverlay setScale(float scaleFactor) {
        this.scaleFactor = scaleFactor;     
        return this;
    }
}
