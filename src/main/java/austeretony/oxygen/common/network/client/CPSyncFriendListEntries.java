package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncFriendListEntries extends ProxyPacket {

    private long[] entriesIds;

    public CPSyncFriendListEntries() {}

    public CPSyncFriendListEntries(long[] entriesIds) {
        this.entriesIds = entriesIds;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        OxygenPlayerData playerData = OxygenManagerServer.instance().getPlayerData(CommonReference.uuid(getEntityPlayerMP(netHandler)));
        buffer.writeShort(this.entriesIds.length);
        for (long entryId : this.entriesIds)
            playerData.getFriendListEntry(entryId).write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++)
            OxygenManagerClient.instance().getPlayerData().addFriendListEntry(FriendListEntry.read(buffer));
        OxygenGUIHelper.dataRecieved(OxygenMain.FRIEND_LIST_SCREEN_ID);
    }
}
