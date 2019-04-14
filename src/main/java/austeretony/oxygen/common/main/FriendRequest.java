package austeretony.oxygen.common.main;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.notification.AbstractNotification;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.notification.EnumNotifications;
import net.minecraft.entity.player.EntityPlayer;

public class FriendRequest extends AbstractNotification {

    public final int index;

    public final UUID senderUUID, targetUUID;

    public final String senderUsername;

    public FriendRequest(int index, UUID targetUUID, UUID senderUUID, String senderUsername) {
        this.index = index;
        this.targetUUID = targetUUID;
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
        OxygenPlayerData senderData = OxygenManagerServer.instance().getPlayerData(this.senderUUID);

        OxygenManagerServer.instance().getPlayerData(this.targetUUID).addFriendListEntry(new FriendListEntry(this.senderUUID, this.senderUsername, false).createId());
        senderData.addFriendListEntry(new FriendListEntry(this.targetUUID, CommonReference.username(player), false).createId());

        OxygenManagerServer.instance().getLoader().savePlayerDataDelegated(this.senderUUID);
        OxygenManagerServer.instance().getLoader().savePlayerDataDelegated(this.targetUUID);

        if (OxygenHelperServer.isOnline(this.senderUUID))
            OxygenHelperServer.sendMessage(CommonReference.playerByUUID(this.senderUUID), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.FRIEND_REQUEST_ACCEPTED_SENDER.ordinal());
        OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.FRIEND_REQUEST_ACCEPTED_TARGET.ordinal());

        senderData.setRequesting(false);
    }

    @Override
    public void rejected(EntityPlayer player) {
        if (OxygenHelperServer.isOnline(this.senderUUID))
            OxygenHelperServer.sendMessage(CommonReference.playerByUUID(this.senderUUID), OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.FRIEND_REQUEST_REJECTED_SENDER.ordinal());
        OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.FRIEND_REQUEST_REJECTED_TARGET.ordinal());

        OxygenManagerServer.instance().getPlayerData(this.senderUUID).setRequesting(false);
    }

    @Override
    public void expired() {
        OxygenManagerServer.instance().getPlayerData(this.senderUUID).setRequesting(false);
    }
}
