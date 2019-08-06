package austeretony.oxygen.client.interaction;

import java.util.LinkedHashSet;
import java.util.Set;

public class InteractionHelperClient {

    public static final Set<IInteractionMenuExecutor> MENU_ACTIONS = new LinkedHashSet<IInteractionMenuExecutor>(6);

    public static final Set<IInteraction> INTERACTIONS = new LinkedHashSet<IInteraction>(5);

    public static void registerInteractionMenuAction(IInteractionMenuExecutor action) {
        MENU_ACTIONS.add(action);
    }

    public static void registerInteraction(IInteraction interaction) {
        INTERACTIONS.add(interaction);
    }

    public static void processInteraction() {
        for (IInteraction interaction : INTERACTIONS)
            if (interaction.isValid())
                interaction.execute();
    }
}
