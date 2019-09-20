package austeretony.oxygen_core.client.interaction;

import java.util.UUID;

import net.minecraft.util.ResourceLocation;

public interface InteractionMenuEntry {

    String getName();

    ResourceLocation getIcon();

    boolean isValid(UUID playerUUID);

    void execute(UUID playerUUID);
}
