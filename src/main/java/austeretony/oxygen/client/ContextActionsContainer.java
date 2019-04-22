package austeretony.oxygen.client;

import java.util.LinkedHashSet;
import java.util.Set;

import austeretony.alternateui.screen.contextmenu.AbstractContextAction;

public class ContextActionsContainer {

    private final Set<AbstractContextAction> actions = new LinkedHashSet<AbstractContextAction>(5);

    public Set<AbstractContextAction> getActions() {
        return this.actions;
    }

    public void addAction(AbstractContextAction action) {
        this.actions.add(action);
    }
}
