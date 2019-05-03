package austeretony.oxygen.client;

import java.util.UUID;

import austeretony.oxygen.client.gui.friendlist.FriendListGUIScreen;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.core.api.ClientReference;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.server.SPChangeStatus;
import austeretony.oxygen.common.network.server.SPEditFriendListEntryNote;
import austeretony.oxygen.common.network.server.SPManageFriendList;
import austeretony.oxygen.common.network.server.SPOxygenRequest;

public class FriendListManagerClient {

    private final OxygenManagerClient manager;

    public FriendListManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void openFriendsListSynced() {
        OxygenMain.network().sendToServer(new SPOxygenRequest(SPOxygenRequest.EnumRequest.OPEN_FRIENDS_LIST));
    }

    public void openFriendsListDelegated() {
        ClientReference.getMinecraft().addScheduledTask(new Runnable() {

            @Override
            public void run() {
                openFriendsList();
            }
        });
    }

    private void openFriendsList() {
        ClientReference.displayGuiScreen(new FriendListGUIScreen());
    }

    public void changeStatusSynced(OxygenPlayerData.EnumStatus status) {
        OxygenMain.network().sendToServer(new SPChangeStatus(status));
    }

    public void downloadFriendsListDataSynced() {
        this.manager.getPlayerData().clearFriendListEntries();
        this.openFriendsListSynced();
    }

    public void sendFriendRequestSynced(UUID playerUUID) {
        OxygenMain.network().sendToServer(new SPManageFriendList(SPManageFriendList.EnumAction.ADD_FRIEND, playerUUID));
    }

    public void removeFriendSynced(UUID playerUUID) {
        this.manager.getPlayerData().removeFriendListEntry(playerUUID);
        OxygenMain.network().sendToServer(new SPManageFriendList(SPManageFriendList.EnumAction.REMOVE_FRIEND, playerUUID));
        OxygenHelperClient.savePlayerDataDelegated(this.manager.getPlayerData());
    }

    public void editFriendListEntryNoteSynced(UUID playerUUID, String note) {
        this.manager.getPlayerData().getFriendListEntryByUUID(playerUUID).setNote(note);
        OxygenMain.network().sendToServer(new SPEditFriendListEntryNote(playerUUID, note));
        OxygenHelperClient.savePlayerDataDelegated(this.manager.getPlayerData());
    }

    public void addToIgnoredSynced(UUID playerUUID) {
        this.manager.getPlayerData().addFriendListEntry(new FriendListEntry(playerUUID, true).createId());
        OxygenMain.network().sendToServer(new SPManageFriendList(SPManageFriendList.EnumAction.ADD_IGNORED, playerUUID));
        OxygenHelperClient.savePlayerDataDelegated(this.manager.getPlayerData());
    }

    public void removeIgnoredSynced(UUID playerUUID) {
        this.manager.getPlayerData().removeFriendListEntry(playerUUID);
        OxygenMain.network().sendToServer(new SPManageFriendList(SPManageFriendList.EnumAction.REMOVE_IGNORED, playerUUID));
        OxygenHelperClient.savePlayerDataDelegated(this.manager.getPlayerData());
    }
}
