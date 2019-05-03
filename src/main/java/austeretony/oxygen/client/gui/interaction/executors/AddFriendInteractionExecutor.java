package austeretony.oxygen.client.gui.interaction.executors;

import java.util.UUID;

import austeretony.oxygen.client.IInteractionExecutor;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.OxygenGUITextures;
import austeretony.oxygen.common.api.OxygenHelperClient;
import net.minecraft.util.ResourceLocation;

public class AddFriendInteractionExecutor implements IInteractionExecutor {

    @Override
    public String getName() {
        return "oxygen.gui.interaction.addFriend";
    }

    @Override
    public ResourceLocation getIcon() {
        return OxygenGUITextures.ADD_TO_FRIENDS_ICONS;
    }

    @Override
    public boolean isValid(UUID playerUUID) {
        return !OxygenHelperClient.isOfflineStatus(playerUUID) && !OxygenHelperClient.getPlayerData().haveFriendListEntryForUUID(playerUUID);
    }

    @Override
    public void execute(UUID playerUUID) {
        OxygenManagerClient.instance().getFriendListManager().sendFriendRequestSynced(playerUUID);
    }
}
