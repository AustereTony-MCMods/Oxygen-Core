package austeretony.oxygen.common.network.server;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.client.CPOxygenCommand;
import austeretony.oxygen.common.network.client.CPSyncGroup;
import austeretony.oxygen.common.network.client.CPSyncValidFriendEntriesIds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPOxygenRequest extends ProxyPacket {

    private EnumRequest request;

    public SPOxygenRequest() {}

    public SPOxygenRequest(EnumRequest request) {
        this.request = request;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.request.ordinal());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        UUID playerUUID = CommonReference.uuid(playerMP);
        this.request = EnumRequest.values()[buffer.readByte()];
        switch (this.request) {
        case SYNC_PRIVILEGED_GROUP:
            OxygenMain.network().sendTo(new CPSyncGroup(), playerMP);
            break;
        case OPEN_FRIENDS_LIST:
            if (!OxygenHelperServer.isSyncing(playerUUID)) {
                OxygenHelperServer.setSyncing(playerUUID, true);
                OxygenManagerServer.instance().syncSharedPlayersData(playerMP, OxygenHelperServer.getSharedDataIdentifiersForScreen(OxygenMain.FRIEND_LIST_SCREEN_ID));
                OxygenMain.network().sendTo(new CPSyncValidFriendEntriesIds(), playerMP);
            }
            break;
        case OPEN_PLAYERS_LIST:
            if (!OxygenHelperServer.isSyncing(playerUUID)) {
                OxygenManagerServer.instance().syncSharedPlayersData(playerMP, OxygenHelperServer.getSharedDataIdentifiersForScreen(OxygenMain.PLAYER_LIST_SCREEN_ID));
                OxygenMain.network().sendTo(new CPOxygenCommand(CPOxygenCommand.EnumCommand.OPEN_PLAYERS_LIST), playerMP);
            }
            break;
        case OPEN_INTERACT_PLAYER_MENU:
            if (!OxygenHelperServer.isSyncing(playerUUID)) {
                OxygenManagerServer.instance().syncSharedPlayersData(playerMP, OxygenHelperServer.getSharedDataIdentifiersForScreen(OxygenMain.INTERACTION_SCREEN_ID));
                OxygenMain.network().sendTo(new CPOxygenCommand(CPOxygenCommand.EnumCommand.OPEN_INTERACT_PLAYER_MENU), playerMP);
            }
            break;
        }
    }

    public enum EnumRequest {

        SYNC_PRIVILEGED_GROUP,
        OPEN_FRIENDS_LIST,
        OPEN_PLAYERS_LIST,
        OPEN_INTERACT_PLAYER_MENU;
    }
}
