package austeretony.oxygen_core.client.interaction;

import java.util.UUID;

public interface PlayerInteractionMenuEntry {

    int getId();

    String getDisplayName(UUID playerUUID);

    boolean isValid(UUID playerUUID);

    boolean process(UUID playerUUID);
}
