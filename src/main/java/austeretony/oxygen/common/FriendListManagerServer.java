package austeretony.oxygen.common;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.EnumOxygenChatMessages;
import austeretony.oxygen.common.main.EnumOxygenPrivileges;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.FriendRequest;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
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
            this.manager.getSharedDataManager().updateStatusData(playerUUID, status);
            if (status == OxygenPlayerData.EnumStatus.OFFLINE)
                this.manager.getLoader().informFriendsLastActivityDelegated(playerUUID);
        }
    }

    public void sendFriendRequest(EntityPlayer sender, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(sender);
        if (OxygenConfig.ENABLE_FRIENDS_LIST.getBooleanValue() 
                && !senderUUID.equals(targetUUID)
                && this.manager.isOnline(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (this.manager.getPlayerData(senderUUID).getFriendsAmount() < OxygenConfig.MAX_FRIENDS.getIntValue()
                    && !senderData.haveFriendListEntryForUUID(targetUUID)) {
                this.manager.sendRequest(sender, CommonReference.playerByUUID(targetUUID), 
                        new FriendRequest(OxygenMain.FRIEND_REQUEST_ID, targetUUID, senderUUID, CommonReference.username(sender)));
            } else
                OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
        }
    }

    public void removeFriend(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            if (!this.manager.playerDataExist(targetUUID)) {
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
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REMOVED.ordinal());
            }
        }
    }

    public void editFriendListEntryNote(EntityPlayerMP player, UUID targetUUID, String note) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            if (note.length() > FriendListEntry.MAX_NOTE_LENGTH)//maximum note length is 40 symbols
                note = note.substring(0, FriendListEntry.MAX_NOTE_LENGTH);
            this.manager.getPlayerData(senderUUID).getFriendListEntryByUUID(targetUUID).setNote(note);
            this.manager.getLoader().savePlayerDataDelegated(senderUUID);
            OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.NOTE_EDITED.ordinal());
        }
    }

    public void addToIgnored(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (OxygenConfig.ENABLE_FRIENDS_LIST.getBooleanValue() 
                && !senderUUID.equals(targetUUID)
                && !PrivilegeProviderServer.getPrivilegeValue(targetUUID, EnumOxygenPrivileges.PREVENT_IGNORE.toString(), false)
                && this.manager.isOnline(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (senderData.getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue() 
                    && !senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.addFriendListEntry(new FriendListEntry(targetUUID, this.manager.getImmutablePlayerData(targetUUID).username, true).createId());
                this.manager.getLoader().savePlayerDataDelegated(senderUUID);
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.ADDED_TO_IGNORED.ordinal());
            } else
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
        } else
            OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
    }

    public void removeIgnored(EntityPlayerMP player, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(player);
        if (!senderUUID.equals(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.removeFriendListEntry(targetUUID);
                this.manager.getLoader().savePlayerDataDelegated(senderUUID);
                OxygenHelperServer.sendMessage(player, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.IGNORED_REMOVED.ordinal());
            }
        }
    }
}
