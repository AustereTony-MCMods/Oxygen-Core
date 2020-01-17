package austeretony.oxygen_core.client.api;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.client.interaction.Interaction;

public class InteractionHelper {

    public static final Set<Interaction> INTERACTIONS = new HashSet<>(5);

    public static void registerInteraction(Interaction interaction) {
        INTERACTIONS.add(interaction);
    }

    public static void processInteractions() {
        for (Interaction interaction : INTERACTIONS)
            if (interaction.isValid())
                interaction.execute();
    }
}
