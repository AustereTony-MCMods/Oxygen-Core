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
import net.minecraft.entity.player.EntityPlayerMP;

public class FriendListManagerServer {

    private final OxygenManagerServer manager;

    public FriendListManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void changeStatus(EntityPlayerMP playerMP, OxygenPlayerData.EnumStatus status) {
        UUID playerUUID = CommonReference.uuid(playerMP);
        OxygenPlayerData playerData = this.manager.getPlayerData(playerUUID);
        if (status != playerData.getStatus()) {
            playerData.setStatus(status);
            OxygenHelperServer.savePlayerDataDelegated(playerUUID, playerData);
            this.manager.getSharedDataManager().updateStatusData(playerUUID, status);
        }
    }

    public void sendFriendRequest(EntityPlayerMP sender, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(sender);
        if (OxygenConfig.ENABLE_FRIEND_LIST.getBooleanValue() 
                && !senderUUID.equals(targetUUID)
                && this.manager.isOnline(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (this.manager.getPlayerData(senderUUID).getFriendsAmount() < OxygenConfig.MAX_FRIENDS.getIntValue()
                    && !senderData.haveFriendListEntryForUUID(targetUUID)) {
                this.manager.sendRequest(sender, CommonReference.playerByUUID(targetUUID), 
                        new FriendRequest(OxygenMain.FRIEND_REQUEST_ID, senderUUID, CommonReference.username(sender)), true);
            } else
                OxygenHelperServer.sendMessage(sender, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
        }
    }

    public void removeFriend(EntityPlayerMP playerMP, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(playerMP);
        if (!senderUUID.equals(targetUUID)) {
            OxygenPlayerData 
            senderData = this.manager.getPlayerData(senderUUID), 
            targetData;  
            if (!this.manager.playerDataExist(targetUUID)) {
                this.manager.createPlayerData(targetUUID);
                targetData = this.manager.getPlayerData(targetUUID);
                OxygenHelperServer.loadPlayerData(targetUUID, targetData);
            } else
                targetData = this.manager.getPlayerData(targetUUID);   
            if (senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.removeFriendListEntry(targetUUID);
                targetData.removeFriendListEntry(senderUUID);
                OxygenHelperServer.removeObservedPlayer(senderUUID, targetUUID, false);
                OxygenHelperServer.removeObservedPlayer(targetUUID, senderUUID, true);
                OxygenHelperServer.savePlayerDataDelegated(senderUUID, senderData);
                OxygenHelperServer.savePlayerDataDelegated(targetUUID, targetData); 
                OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.FRIEND_REMOVED.ordinal());
            }
        }
    }

    public void editFriendListEntryNote(EntityPlayerMP playerMP, UUID targetUUID, String note) {
        UUID senderUUID = CommonReference.uuid(playerMP);
        if (!senderUUID.equals(targetUUID)) {
            if (note.length() > FriendListEntry.MAX_NOTE_LENGTH)//maximum note length is 40 symbols
                note = note.substring(0, FriendListEntry.MAX_NOTE_LENGTH);
            OxygenPlayerData playerData = this.manager.getPlayerData(senderUUID);
            playerData.getFriendListEntryByUUID(targetUUID).setNote(note);
            OxygenHelperServer.savePlayerDataDelegated(senderUUID, playerData);
            OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.NOTE_EDITED.ordinal());
        }
    }

    public void addToIgnored(EntityPlayerMP playerMP, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(playerMP);
        if (OxygenConfig.ENABLE_FRIEND_LIST.getBooleanValue() 
                && !senderUUID.equals(targetUUID)
                && !PrivilegeProviderServer.getPrivilegeValue(targetUUID, EnumOxygenPrivileges.PREVENT_IGNORE.toString(), false)
                && this.manager.isOnline(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (senderData.getIgnoredAmount() < OxygenConfig.MAX_IGNORED.getIntValue() 
                    && !senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.addFriendListEntry(new FriendListEntry(targetUUID, true).createId());
                OxygenHelperServer.addObservedPlayer(senderUUID, targetUUID, true);
                OxygenHelperServer.savePlayerDataDelegated(senderUUID, senderData);
                OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.ADDED_TO_IGNORED.ordinal());
            } else
                OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
        } else
            OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.REQUEST_RESET.ordinal());
    }

    public void removeIgnored(EntityPlayerMP playerMP, UUID targetUUID) {
        UUID senderUUID = CommonReference.uuid(playerMP);
        if (!senderUUID.equals(targetUUID)) {
            OxygenPlayerData senderData = this.manager.getPlayerData(senderUUID);
            if (senderData.haveFriendListEntryForUUID(targetUUID)) {
                senderData.removeFriendListEntry(targetUUID);
                OxygenHelperServer.removeObservedPlayer(senderUUID, targetUUID, true);
                OxygenHelperServer.savePlayerDataDelegated(senderUUID, senderData);
                OxygenHelperServer.sendMessage(playerMP, OxygenMain.OXYGEN_MOD_INDEX, EnumOxygenChatMessages.IGNORED_REMOVED.ordinal());
            }
        }
    }
}
