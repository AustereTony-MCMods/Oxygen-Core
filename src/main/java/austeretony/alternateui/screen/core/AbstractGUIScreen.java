package austeretony.alternateui.screen.core;

import java.io.IOException;
import java.util.Iterator;

import org.lwjgl.input.Mouse;

import austeretony.alternateui.screen.browsing.GUIScroller.GUIEnumScrollerType;
import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.list.GUIDropDownElement;
import austeretony.alternateui.screen.list.GUIDropDownList;
import austeretony.alternateui.screen.panel.GUIButtonPanel;
import austeretony.alternateui.screen.panel.GUIButtonPanel.GUIEnumOrientation;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;

/**
 * Модернизированный GuiScreen. Должен быть унаследован вашим ГПИ простого интерфейса.
 * 
 * @author AustereTony
 */
public abstract class AbstractGUIScreen extends GuiScreen {

    public int 
    xSize = 176,
    ySize = 166,
    guiLeft, guiTop;

    /** Рабочее пространство */
    private GUIWorkspace workspace;

    protected boolean workspaceCreated, isScrolling;

    private int yPrevMouse;

    @Override
    public void initGui() {   	    	
        this.workspace = this.initWorkspace();       
        this.init();       
        this.workspace.setCurrentSection(this.getDefaultSection());        
        this.xSize = this.workspace.getWidth();
        this.ySize = this.workspace.getHeight();               
        this.guiLeft = this.workspace.getX();
        this.guiTop = this.workspace.getY();          
        this.workspaceCreated = true;
    }

    private void init() {  	
        this.initSections();    	
        for (AbstractGUISection section : this.getWorkspace().getSections())   		
            section.init();
    }

    /**
     * Используется для инициализации объекта GUIWorkspace (рабочего пространства ГПИ).
     * 
     * @return новый GUIWorkspace
     */
    protected abstract GUIWorkspace initWorkspace();

    /**
     * Используется для добавления неизменяемых элементов в разделы. Вызывается один раз при инициализации ГПИ. Рекомендуется инициализировать здесь все разделы.
     */
    protected abstract void initSections();

    /**
     * Определяет раздел, отображающийся при открытии ГПИ. 
     * 
     * @return GUISection по умолчанию
     */
    protected abstract AbstractGUISection getDefaultSection();

    public GUIWorkspace getWorkspace() {    	
        return this.workspace;
    }

    protected boolean isWorkspaceCreated() {    	
        return this.workspaceCreated;
    }

    @Override
    public void drawDefaultBackground() {}

    /**
     * Для рендера перед рендером фона раздела.
     */
    protected void drawBackgroundLayer(int mouseX, int mouseY, float partialTicks) {}

    /**
     * Для рендера после рендера фона раздела.
     */
    protected void drawForegroundLayer(int mouseX, int mouseY, float partialTicks) {}

    //TODO drawScreen()
    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {   	
        if (this.isWorkspaceCreated()) {         	
            this.drawDefaultBackground();       	
            this.getWorkspace().draw(mouseX, mouseY);       	
            AbstractGUISection section = this.getWorkspace().getCurrentSection();       	
            section.drawBackground();       	
            this.drawBackgroundLayer(mouseX, mouseY, partialTicks);        	        	        	               	
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) this.guiLeft, (float) this.guiTop, 0.0F);	     	        
            RenderHelper.disableStandardItemLighting();        	
            section.mouseOver(mouseX - this.guiLeft, mouseY - this.guiTop);	        	        
            section.draw(mouseX - this.guiLeft, mouseY - this.guiTop);	        	    	        
            this.drawForegroundLayer(mouseX, mouseY, partialTicks);        	
            GlStateManager.disableLighting();     
            GlStateManager.enableDepth();         	
            section.drawTooltip(mouseX - this.guiLeft, mouseY - this.guiTop);       
            GlStateManager.disableDepth();      
            GlStateManager.enableLighting();      			
            GlStateManager.popMatrix();
            GlStateManager.disableLighting();     
            GlStateManager.enableDepth();  
            section.drawContextMenu(mouseX, mouseY);
            if (section.hasCurrentCallback()) {
                section.getCurrentCallback().mouseOver(mouseX, mouseY);
                section.drawCallback(mouseX, mouseY);                      
                section.drawCallbackTooltip(mouseX, mouseY);  
                section.drawCallbackContextMenu(mouseX, mouseY);
            }
            GlStateManager.disableDepth();      
            GlStateManager.enableLighting(); 
            RenderHelper.enableGUIStandardItemLighting();	        	        
        }      
        this.yPrevMouse = mouseY;
    }

    public void handlePanelSlidebar(GUIButtonPanel buttonPanel, int mouseY) {  
        int slidebarOffset;        
        float sliderActiveHeight, currentPosition;   	
        if (buttonPanel.getScroller().getSlider().isDragged()) {   
            slidebarOffset = mouseY - buttonPanel.getScroller().getSlider().getSlidebarY() - this.guiTop;				
            sliderActiveHeight = buttonPanel.getScroller().getSlider().getHeight() - buttonPanel.getScroller().getSlider().getSlidebarHeight();				
            currentPosition = mouseY - slidebarOffset - this.yPrevMouse + mouseY - this.guiTop;				        				
            buttonPanel.getScroller().setPosition((int) ((float) buttonPanel.getScroller().getMaxPosition() * ((currentPosition - buttonPanel.getScroller().getSlider().getY()) / sliderActiveHeight)));       									
            buttonPanel.getScroller().getSlider().handleSlidebarViaCursor((int) (sliderActiveHeight * (currentPosition / sliderActiveHeight)));				
            this.scrollButtonPanel(buttonPanel);    				
        }
    }

    /**
     * Вызывается когда по элементу осуществлён клик.
     * 
     * @param section раздел, к которому относится элемент
     * @param element элемент, который был активирован
     */
    public abstract void handleElementClick(AbstractGUISection section, GUIBaseElement element);

    //TODO mouseClicked()
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {    	
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.getWorkspace().getCurrentSection().mouseClicked(mouseX, mouseY, mouseButton);
    }

    //TODO keyTyped()
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {   	
        super.keyTyped(typedChar, keyCode);            		
        this.getWorkspace().getCurrentSection().keyTyped(typedChar, keyCode);   
    }

    //TODO handleMouseInput()
    @Override
    public void handleMouseInput() throws IOException {   	
        super.handleMouseInput();               	
        this.isScrolling = Mouse.getDWheel() != 0;       
        if (this.isWorkspaceCreated())
            this.workspace.getCurrentSection().handleScroller(this.isScrolling);  
    }

    //TODO scrollDropDownList()
    public void scrollDropDownList(GUIDropDownList dropDownList) {   	
        int i = 0, size;    	
        GUIDropDownElement dropDownElement;   	    		
        dropDownList.visibleElements.clear();   	
        if (dropDownList.getScroller().scrollerType == GUIEnumScrollerType.STANDARD) {   		   		
            for (i = dropDownList.getScroller().getPosition(); i < dropDownList.getScroller().getPosition() + dropDownList.getScroller().rowsVisible; i++) {   		
                if (i < dropDownList.elementsBuffer.size()) {   			
                    dropDownElement = dropDownList.elementsBuffer.get(i);     			            	            	
                    size = dropDownList.visibleElements.size();					    				
                    dropDownElement.setPosition(dropDownList.getX(), dropDownList.getY() + (size + 1) * dropDownList.getHeight() - (size / dropDownList.getVisibleElementsAmount()) * (dropDownList.getMaxElementsAmount() * dropDownList.getHeight()));   				
                    dropDownList.visibleElements.add(dropDownElement);
                }
            }
        }//TODO Handle smooth scroller
    }

    //TODO scrollButtonPanel()
    public void scrollButtonPanel(GUIButtonPanel panel) {       
        int i = 0, size, k;
        boolean scrollingSearch = !panel.searchButtons.isEmpty();
        GUIButton button;
        panel.visibleButtons.clear();
        if (panel.getScroller().scrollerType == GUIEnumScrollerType.STANDARD) {
            for (i = panel.getScroller().getPosition(); i < panel.getScroller().getPosition() + panel.getVisibleElementsAmount(); i++) {
                if ((!scrollingSearch && i < panel.buttonsBuffer.size()) || (scrollingSearch && i < panel.searchButtons.size())) {
                    button = scrollingSearch ? panel.searchButtons.get(i) : panel.buttonsBuffer.get(i);  
                    size = panel.visibleButtons.size();
                    button.setPosition(panel.orientation == GUIEnumOrientation.HORIZONTAL ? panel.getX() + size * (panel.getButtonWidth() + panel.getButtonsOffset()) - (size / panel.getVisibleElementsAmount()) * (panel.getMaxElementsAmount() * (panel.getButtonWidth() + panel.getButtonsOffset())) : panel.getX(),
                            panel.orientation == GUIEnumOrientation.VERTICAL ? panel.getY() + size * (panel.getButtonHeight() + panel.getButtonsOffset()) - (size / panel.getVisibleElementsAmount()) * (panel.getMaxElementsAmount() * (panel.getButtonHeight() + panel.getButtonsOffset())) : panel.getY());   
                    panel.visibleButtons.add(button);
                }
            }
        }//TODO Handle smooth scroller
    }

    //TODO searchButtonPanel()
    public void searchButtonPanel(GUIButtonPanel panel) {      
        int buttonIndex, size, k;       
        GUIButton buttonCopy, button;        
        String 
        typedText = panel.getSearchField().getTypedText().toLowerCase(),
        buttonName;                
        Iterator<GUIButton> buttonsIterator = panel.buttonsBuffer.iterator();           
        panel.visibleButtons.clear();  
        panel.searchButtons.clear();  
        while (buttonsIterator.hasNext()) {             
            button = buttonsIterator.next();    
            if (button != null) {                               
                buttonName = button.getDisplayText().toLowerCase();             
                if (buttonName.startsWith(typedText) || buttonName.contains(" " + typedText)) {                                                                                                 
                    size = panel.searchButtons.size(); 
                    buttonCopy = button;                                                                                                                                           
                    k = size;                                           
                    buttonCopy.setY(panel.getY() + k * (panel.getButtonHeight() + panel.getButtonsOffset()) - (size / panel.getMaxElementsAmount()) * (panel.getMaxElementsAmount() * (panel.getButtonHeight() + panel.getButtonsOffset())));         
                    if (size < panel.getVisibleElementsAmount())                          
                        panel.visibleButtons.add(buttonCopy);    
                    panel.searchButtons.add(buttonCopy);
                }
            }
        }
    }

    protected abstract boolean doesGUIPauseGame();

    @Override
    public boolean doesGuiPauseGame() {
        return this.doesGUIPauseGame();
    }

    //TODO updateScreen()
    @Override
    public void updateScreen() {    	
        if (this.isWorkspaceCreated())	
            this.getWorkspace().getCurrentSection().updateScreen();    		
    }

    //TODO close()
    /**
     * Закрывает ГПИ.
     */
    public void close() {
        this.mc.displayGuiScreen(null);
        this.mc.setIngameFocus();
    }

    @Override
    public void onGuiClosed() {
        GUIBaseElement.resetDragged();
    }

    /**
     * Возвращает длину переданной строки.
     * 
     * @param text
     * 
     * @return длина строки в пикселях
     */
    public int width(String text) {   	
        return this.mc.fontRenderer.getStringWidth(text);
    }
}
