package austeretony.oxygen.client.gui.interaction;

import java.util.UUID;

import austeretony.oxygen.client.IInteractionExecutor;
import net.minecraft.util.ResourceLocation;

public class CloseScreenInteractionExecutor implements IInteractionExecutor {

    @Override
    public String getName() {
        return "oxygen.gui.interaction.close";
    }

    @Override
    public ResourceLocation getIcon() {
        return null;//unused
    }

    @Override
    public boolean isValid(UUID playerUUID) {
        return true;
    }

    @Override
    public void execute(UUID playerUUID) {}
}
