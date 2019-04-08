package austeretony.alternateui.screen.panel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.alternateui.screen.text.GUITextField;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class SearchableGUIButtonPanel extends GUIButtonPanel {

    private boolean hasSearchField, searched, updated;

    private GUITextField searchField;

    private int buttonsAmount, index;

    public final List<GUIButton> searchButtons = new ArrayList<GUIButton>();

    public final Map<Integer, GUIButton> buttons = new HashMap<Integer, GUIButton>();   

    public SearchableGUIButtonPanel(int xPosition, int yPosition, int buttonWidth, int buttonHeight, int buttonsAmount) {
        super(GUIEnumOrientation.VERTICAL, xPosition, yPosition, buttonWidth, buttonHeight);
        this.buttonsAmount = buttonsAmount;
    }

    public boolean hasSearchField() {	
        return this.hasSearchField;
    }

    public void initSearchField(GUITextField searchField) {		
        this.searchField = this.searchField == null ? searchField : this.searchField;		
        this.hasSearchField = true;
    }	

    public GUITextField getSearchField() {	
        return this.searchField;
    }

    public GUIButtonPanel addButton(GUIButton button) {
        super.addButton(button);
        this.buttons.put(this.index, button);
        this.index++;
        return this;
    }
    
    public void reset() {
        this.visibleButtons.clear();
        this.buttonsBuffer.clear();
        this.searchButtons.clear();
        this.buttons.clear();
        this.index = 0;
    }

    @Override
    public void update() {   
        if (this.isEnabled()) {
            if (this.hasSearchField()) {       	          		
                if (!this.getSearchField().getTypedText().isEmpty()) {   
                    this.searched = true;
                    this.updateSearchResult();
                } else {
                    if (!this.updated && this.searched) {
                        this.updated = true;
                        this.searched = false;
                        this.updateSearchResult();
                    }          			
                }
            }
        }
    }

    public void updateSearchResult() {    	
        int buttonIndex, size, k;       
        GUIButton buttonCopy, button;        
        String 
        typedText = this.getSearchField().getTypedText().toLowerCase(),
        buttonName;                
        Iterator<Integer> buttonsIterator = this.buttons.keySet().iterator();   	
        this.visibleButtons.clear();    	
        this.searchButtons.clear();        
        while (buttonsIterator.hasNext()) {        	
            buttonIndex = buttonsIterator.next();       	
            button = this.buttons.get(buttonIndex);       	
            if (button != null) {        	        	
                buttonName = button.getDisplayText().toLowerCase();       	
                if (buttonName.startsWith(typedText) || buttonName.contains(" " + typedText)) {       		        					        			
                    size = this.searchButtons.size();       					
                    buttonCopy = this.buttonsBuffer.get(buttonIndex);        					        											
                    k = size;						
                    buttonCopy.setY(this.getY() + k * (this.getButtonHeight() + this.getButtonsOffset()) - (size / this.buttonsAmount) * (this.buttonsAmount * (this.getButtonHeight() + this.getButtonsOffset())));					
                    this.visibleButtons.add(buttonCopy);		       			
                    this.searchButtons.add(buttonCopy);
                }
            }
        }
    }
}
