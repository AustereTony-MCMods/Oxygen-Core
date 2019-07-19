package austeretony.oxygen.client.interaction;

import java.util.UUID;

import net.minecraft.util.ResourceLocation;

public interface IInteractionMenuExecutor {

    String getName();

    ResourceLocation getIcon();

    boolean isValid(UUID playerUUID);

    void execute(UUID playerUUID);
}
