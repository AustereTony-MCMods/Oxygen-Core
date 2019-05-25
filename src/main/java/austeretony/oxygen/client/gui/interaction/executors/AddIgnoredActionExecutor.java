package austeretony.oxygen.client.gui.interaction.executors;

import java.util.UUID;

import austeretony.oxygen.client.IInteractionExecutor;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import net.minecraft.util.ResourceLocation;

public class AddIgnoredActionExecutor implements IInteractionExecutor {

    @Override
    public String getName() {
        return "oxygen.gui.interaction.ignore";
    }

    @Override
    public ResourceLocation getIcon() {
        return OxygenGUITextures.IGNORED_ICONS;
    }

    @Override
    public boolean isValid(UUID playerUUID) {
        return !OxygenHelperClient.isOfflineStatus(playerUUID) && !OxygenHelperClient.getPlayerData().haveFriendListEntryForUUID(playerUUID);
    }

    @Override
    public void execute(UUID playerUUID) {
        OxygenManagerClient.instance().getFriendListManager().addToIgnoredSynced(playerUUID);
    }
}
