package austeretony.oxygen_core.client.interaction;

import net.minecraft.client.settings.KeyBinding;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class InteractionHelper {

    private static final Map<Integer, EntityInteraction> INTERACTIONS_MAP = new HashMap<>();
    private static final Map<Integer, PlayerInteractionMenuEntry> PLAYER_INTERACTION_MENU_ENTRIES_MAP = new HashMap<>();

    @Nullable
    private static KeyBinding interactionKeyBinding;

    private InteractionHelper() {}

    public static Map<Integer, EntityInteraction> getInteractionsMap() {
        return INTERACTIONS_MAP;
    }

    public static void addInteraction(EntityInteraction entry) {
        INTERACTIONS_MAP.put(entry.getId(), entry);
    }

    public static Map<Integer, PlayerInteractionMenuEntry> getPlayerInteractionsMap() {
        return PLAYER_INTERACTION_MENU_ENTRIES_MAP;
    }

    public static void addPlayerInteraction(PlayerInteractionMenuEntry entry) {
        PLAYER_INTERACTION_MENU_ENTRIES_MAP.put(entry.getId(), entry);
    }

    @Nullable
    public static KeyBinding getInteractionKeyBinding() {
        return interactionKeyBinding;
    }

    public static void setInteractionKeyBinding(KeyBinding keyBinding) {
        interactionKeyBinding = keyBinding;
    }
}
