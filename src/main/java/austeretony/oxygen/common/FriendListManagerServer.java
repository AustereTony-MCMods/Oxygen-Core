package austeretony.oxygen.common;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.EnumChatMessages;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.FriendRequest;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class FriendListManagerServer {

    private final OxygenManagerServer manager;

    public FriendListManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void changeStatus(EntityPlayer player, OxygenPlayerData.EnumStatus status) {
        UUID playerUUID = CommonReference.uuid(player);
        if (status != OxygenHelperServer.getPlayerStatus(playerUUID)) {
            this.manager.getPlayerData(playerUUID).setStatus(status);
            this.manager.getLoader().savePlayerDataDelegated(playerUUID);
            this.updateSharedStatusData(playerUUID);
            this.manager.syncSharedPlayersData(player, OxygenMain.STATUS_DATA_ID);//probably this is not important
            OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.STATUS_CHANGED.ordinal(), status.toString().toLowerCase());
        }
    }

    private void updateSharedStatusData(UUID playerUUID) {
        this.manager.getSharedPlayerData(playerUUID).getData(OxygenMain.STATUS_DATA_ID).put(0, (byte) this.manager.getPlayerData(playerUUID).getStatus().ordinal());
    }

    public void sendFriendRequest(EntityPlayer sender, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(sender);
        if (OxygenConfig.ENABLE_FRIENDS_LIST.getBooleanValue() 
                && !senderUUID.equals(targetUUID)) {
            if (this.manager.isOnline(targetUUID)) {
                OxygenPlayerData 
                senderData = this.manager.getPlayerData(senderUUID),
                targetData = this.manager.getPlayerData(targetUUID);
                if (senderData.getFriendsAmount() < OxygenConfig.MAX_FRIENDS.getIntValue()
                        && !senderData.haveFriendListEntryForUUID(targetUUID)
                        && !senderData.isRequesting() 
                        && targetData.getStatus() != OxygenPlayerData.EnumStatus.OFFLINE) {
                    if (!targetData.haveFriendListEntryForUUID(senderUUID) || !targetData.getFriendListEntryByUUID(senderUUID).ignored) {
                        this.manager.addNotification(CommonReference.playerByUUID(targetUUID), 
                                new FriendRequest(OxygenMain.FRIEND_REQUEST_ID, targetUUID, senderUUID, CommonReference.username(sender)));
                    } else
                        OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.SENDER_IGNORED.ordinal());
                } else
                    OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.REQUEST_RESET.ordinal());
            } else
                OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.TARGET_OFFLINE.ordinal());
        }
    }

    public void removeFriend(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            if (!this.manager.isOnline(targetUUID)) {
                this.manager.createPlayerData(targetUUID);
                this.manager.getLoader().loadPlayerData(targetUUID);
            }
            OxygenPlayerData 
            senderData = this.manager.getPlayerData(senderUUID),
            targetData = this.manager.getPlayerData(targetUUID);   
            if (senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.removeFriendListEntry(targetUUID);
                targetData.removeFriendListEntry(senderUUID);
                this.manager.getLoader().savePlayerDataDelegated(senderUUID);
                this.manager.getLoader().savePlayerDataDelegated(targetUUID); 
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.FRIEND_REMOVED.ordinal());
            }
        }
    }

    public void editFriendListEntryNote(EntityPlayerMP player, UUID targetUUID, String note) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            if (note.length() > FriendListEntry.MAX_DESCRIPTION_LENGTH)//maximum note length is 32 symbols
                note = note.substring(0, FriendListEntry.MAX_DESCRIPTION_LENGTH);
            this.manager.getPlayerData(senderUUID).getFriendListEntryByUUID(targetUUID).setNote(note);
            this.manager.getLoader().savePlayerDataDelegated(senderUUID);
            OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.NOTE_EDITED.ordinal());
        }
    }

    public void addToIgnored(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            if (this.manager.isOnline(targetUUID)) {
                OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
                if (senderData.getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue() && !senderData.haveFriendListEntryForUUID(targetUUID)) {
                    senderData.addFriendListEntry(new FriendListEntry(targetUUID, this.manager.getSharedPlayerData(targetUUID).getUsername(), true).createId());
                    this.manager.getLoader().savePlayerDataDelegated(senderUUID);
                    OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.ADDED_TO_IGNORED.ordinal());
                } else
                    OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.REQUEST_RESET.ordinal());
            } else
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.TARGET_OFFLINE.ordinal());
        }
    }

    public void removeIgnored(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.removeFriendListEntry(targetUUID);
                this.manager.getLoader().savePlayerDataDelegated(senderUUID);
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumChatMessages.IGNORED_REMOVED.ordinal());
            }
        }
    }
}
