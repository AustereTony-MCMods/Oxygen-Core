package austeretony.oxygen.common.main;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.notification.AbstractNotification;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.notification.EnumNotifications;
import net.minecraft.entity.player.EntityPlayer;

public class FriendRequest extends AbstractNotification {

    public final int index;

    public final UUID senderUUID;

    public final String senderUsername;

    public FriendRequest(int index, UUID senderUUID, String senderUsername) {
        this.index = index;
        this.senderUUID = senderUUID;
        this.senderUsername = senderUsername;
    }

    @Override
    public EnumNotifications getType() {
        return EnumNotifications.REQUEST;
    }

    @Override
    public String getDescription() {
        return "oxygen.request.friendRequest";
    }

    @Override
    public String[] getArguments() {
        return new String[] {this.senderUsername};
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public int getExpireTime() {
        return OxygenConfig.FRIEND_REQUEST_EXPIRE_TIME.getIntValue();
    }

    @Override
    public void accepted(EntityPlayer player) {
        UUID targetUUID = CommonReference.uuid(player);
        OxygenPlayerData 
        senderData = OxygenHelperServer.getPlayerData(this.senderUUID),
        targetData = OxygenHelperServer.getPlayerData(targetUUID);

        targetData.addFriendListEntry(new FriendListEntry(this.senderUUID, false).createId()); 
        senderData.addFriendListEntry(new FriendListEntry(targetUUID, false).createId());

        OxygenHelperServer.addObservedPlayer(this.senderUUID, targetUUID, false);
        OxygenHelperServer.addObservedPlayer(targetUUID, this.senderUUID, true);

        OxygenHelperServer.savePlayerDataDelegated(this.senderUUID, senderData);
        OxygenHelperServer.savePlayerDataDelegated(targetUUID, targetData);

        if (OxygenHelperServer.isOnline(this.senderUUID))
            OxygenHelperServer.sendMessage(CommonReference.playerByUUID(this.senderUUID), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REQUEST_ACCEPTED_SENDER.ordinal());

        OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REQUEST_ACCEPTED_TARGET.ordinal());

        senderData.setRequesting(false);
    }

    @Override
    public void rejected(EntityPlayer player) {
        if (OxygenHelperServer.isOnline(this.senderUUID))
            OxygenHelperServer.sendMessage(CommonReference.playerByUUID(this.senderUUID), OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REQUEST_REJECTED_SENDER.ordinal());
        OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REQUEST_REJECTED_TARGET.ordinal());

        OxygenHelperServer.setRequesting(this.senderUUID, false);
    }

    @Override
    public void expired() {
        OxygenHelperServer.setRequesting(this.senderUUID, false);
    }
}
