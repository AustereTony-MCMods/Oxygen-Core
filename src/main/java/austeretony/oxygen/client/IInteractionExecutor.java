package austeretony.oxygen.client;

import java.util.UUID;

import net.minecraft.util.ResourceLocation;

public interface IInteractionExecutor {

    String getName();

    ResourceLocation getIcon();

    boolean isValid(UUID playerUUID);

    void execute(UUID playerUUID);
}
