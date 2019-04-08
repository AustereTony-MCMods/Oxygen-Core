package austeretony.alternateui.screen.list;

import austeretony.alternateui.screen.core.GUIAdvancedElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Элемент выпадающего списка.
 * 
 * @author AustereTony
 */
@SideOnly(Side.CLIENT)
public class GUIDropDownElement extends GUIAdvancedElement<GUIDropDownElement> {

    public GUIDropDownElement() {		
        this.enableDynamicBackground();		
        this.enableFull();		
    }
}
