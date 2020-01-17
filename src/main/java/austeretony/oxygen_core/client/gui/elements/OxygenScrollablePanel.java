package austeretony.oxygen_core.client.gui.elements;

import austeretony.alternateui.screen.browsing.GUIScroller;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.button.GUISlider;
import austeretony.alternateui.screen.core.AbstractGUIScreen;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.util.EnumGUIOrientation;
import austeretony.oxygen_core.client.api.EnumBaseGUISetting;

public class OxygenScrollablePanel extends GUIButtonPanel {

    private ClickListener clickListener;

    private GUIButton previousButton;

    public OxygenScrollablePanel(AbstractGUIScreen screen, int xPosition, int yPosition, int buttonWidth, int buttonHeight, int buttonsOffset, int scrollerRows, int visibleScrollerRows, float textScale, boolean enableSlider) { 
        super(EnumGUIOrientation.VERTICAL, xPosition, yPosition, buttonWidth, buttonHeight);
        this.initScreen(screen);
        this.setPosition(xPosition, yPosition); 
        this.setButtonsOffset(buttonsOffset);
        GUIScroller scroller = new GUIScroller(scrollerRows, visibleScrollerRows);
        this.initScroller(scroller);
        GUISlider slider = new GUISlider(xPosition + buttonWidth + 1, yPosition, 2, visibleScrollerRows * buttonHeight + buttonsOffset * (visibleScrollerRows - 1));
        this.setSize(buttonWidth, slider.getSlidebarHeight());
        slider.setDynamicBackgroundColor(EnumBaseGUISetting.SLIDER_ENABLED_COLOR.get().asInt(), EnumBaseGUISetting.SLIDER_DISABLED_COLOR.get().asInt(), EnumBaseGUISetting.SLIDER_HOVERED_COLOR.get().asInt());
        if (enableSlider)
            scroller.initSlider(slider); 
        this.setSize(buttonWidth, buttonHeight * scroller.rowsVisible + this.getButtonsOffset() * (scroller.rowsVisible - 1));      
        this.setTextScale(textScale);
        this.enableFull();
    }

    public <T> void setClickListener(ClickListener<T> listener) {
        this.clickListener = listener;
    }

    @Override
    public void drawTooltip(int mouseX, int mouseY) {           
        if (this.isVisible() && (!this.hasContextMenu() || (this.hasContextMenu() && !this.getContextMenu().isDragged())))                   
            for (GUIButton button : this.visibleButtons)                
                button.drawTooltip(mouseX, mouseY);             
    }

    @Override
    public boolean mouseClicked(int mouseX, int mouseY, int mouseButton) {
        if (this.isEnabled()) {  
            if (this.hasContextMenu() && this.getContextMenu().isDragged() && mouseButton != 1)                       
                this.getContextMenu().mouseClicked(mouseX, mouseY, mouseButton);
            for (GUIButton button : this.visibleButtons) {
                if (button.mouseClicked(mouseX, mouseY, mouseButton)) { 
                    if (this.clickListener != null) {
                        this.clickListener.onClick(this.previousButton, button, mouseX, mouseY, mouseButton);
                        this.previousButton = button;
                    }
                }
                if (button.isHovered() && mouseButton == 1) {
                    if (this.hasContextMenu() && !this.getContextMenu().isDragged())
                        this.getContextMenu().open(button, mouseX, mouseY);
                }
            }
        }
        return false;
    }

    public void setPreviousClickedButton(GUIButton button) {
        this.previousButton = button;
    }

    public static interface ClickListener<T> {

        void onClick(T previous, T clicked, int mouseX, int mouseY, int mouseButton);
    }
}