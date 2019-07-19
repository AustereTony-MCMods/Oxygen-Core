package austeretony.oxygen.client.interaction;

import java.util.LinkedHashSet;
import java.util.Set;

public class InteractionHelperClient {

    public static final int MAX_MENU_ACTIONS_AMOUNT = 6;//only 6 shared actions for player interaction menu (7 in total)

    public static final Set<IInteractionMenuExecutor> MENU_ACTIONS = new LinkedHashSet<IInteractionMenuExecutor>(MAX_MENU_ACTIONS_AMOUNT);

    public static final Set<IInteractionOverlay> INTERACTION_OVERLAYS = new LinkedHashSet<IInteractionOverlay>(MAX_MENU_ACTIONS_AMOUNT);

    public static final Set<IInteraction> INTERACTIONS = new LinkedHashSet<IInteraction>(MAX_MENU_ACTIONS_AMOUNT);

    public static void registerInteractionMenuAction(IInteractionMenuExecutor action) {
        MENU_ACTIONS.add(action);
    }

    public static void registerInteractionOverlay(IInteractionOverlay overlay) {
        INTERACTION_OVERLAYS.add(overlay);
    }

    public static void registerInteraction(IInteraction interaction) {
        INTERACTIONS.add(interaction);
    }

    public static void processInteraction() {
        for (IInteraction interaction : INTERACTIONS) {
            if (interaction.isValid())
                interaction.execute();
        }
    }
}
