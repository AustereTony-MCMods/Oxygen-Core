package austeretony.oxygen_core.client.interaction;

import java.util.LinkedHashSet;
import java.util.Set;

public class InteractionHelperClient {

    public static final Set<InteractionMenuEntry> MENU_ENTRIES = new LinkedHashSet<>(7);

    public static final Set<Interaction> INTERACTIONS = new LinkedHashSet<>(7);

    public static void registerInteractionMenuEntry(InteractionMenuEntry action) {
        MENU_ENTRIES.add(action);
    }

    public static void registerInteraction(Interaction interaction) {
        INTERACTIONS.add(interaction);
    }

    public static void processInteraction() {
        for (Interaction interaction : INTERACTIONS)
            if (interaction.isValid())
                interaction.execute();
    }
}
