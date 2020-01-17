package austeretony.oxygen_core.client.interaction;

import java.util.UUID;

public interface PlayerInteractionMenuEntry {

    String getLocalizedName();

    boolean isValid(UUID playerUUID);

    void execute(UUID playerUUID);
}
