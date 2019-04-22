package austeretony.oxygen.client.gui;

import austeretony.alternateui.screen.core.GUISimpleElement;
import austeretony.oxygen.client.gui.settings.GUISettings;

public class BackgroundGUIFiller extends GUISimpleElement<BackgroundGUIFiller> {

    protected int textureWidth, textureHeight;

    public BackgroundGUIFiller(int xPosition, int yPosition, int width, int height) {             
        this.setPosition(xPosition, yPosition);         
        this.setSize(width, height);
        this.enableFull();
        this.textureWidth = width + GUISettings.instance().getTextureOffsetX() * 2;
        this.textureHeight = height + GUISettings.instance().getTextureOffsetY() * 2;
    }
}
