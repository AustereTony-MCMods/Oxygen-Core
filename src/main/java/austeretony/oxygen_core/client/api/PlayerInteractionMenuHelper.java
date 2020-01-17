package austeretony.oxygen_core.client.api;

import java.util.Set;
import java.util.TreeSet;

import austeretony.oxygen_core.client.interaction.PlayerInteractionMenuEntry;

public class PlayerInteractionMenuHelper {

    public static final Set<PlayerInteractionMenuEntry> MENU_ENTRIES = new TreeSet<>((e1, e2)->e1.getLocalizedName().compareTo(e2.getLocalizedName()));

    public static void registerInteractionMenuEntry(PlayerInteractionMenuEntry action) {
        MENU_ENTRIES.add(action);
    }
}
