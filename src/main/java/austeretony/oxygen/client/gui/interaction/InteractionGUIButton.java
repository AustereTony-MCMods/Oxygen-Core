package austeretony.oxygen.client.gui.interaction;

import austeretony.alternateui.screen.button.GUIButton;
import austeretony.oxygen.client.IInteractionExecutor;

public class InteractionGUIButton extends GUIButton {

    public final IInteractionExecutor action;

    public InteractionGUIButton(int x, int y, int width, int height, IInteractionExecutor action) {
        super(x, y, width, height);
        this.action = action;
    }
}
