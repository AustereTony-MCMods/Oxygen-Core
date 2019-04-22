package austeretony.oxygen.common.network.client;

import java.util.Set;

import austeretony.oxygen.common.api.OxygenGUIHelper;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.server.SPSendAbsentFriendListEntriesIds;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncValidFriendEntriesIds extends ProxyPacket {

    public CPSyncValidFriendEntriesIds() {}

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        Set<Long> entriesIds = OxygenHelperServer.getPlayerData(CommonReference.uuid(getEntityPlayerMP(netHandler))).getFriendListEntriesIds();
        buffer.writeShort(entriesIds.size());
        for (long entryId : entriesIds)
            buffer.writeLong(entryId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        long[] syncedIds = new long[buffer.readShort()];
        int 
        i = 0, 
        j = 0;
        for (; i < syncedIds.length; i++)
            syncedIds[i] = buffer.readLong();
        long[] needSync = new long[syncedIds.length];
        Set<Long> friendsIds = OxygenHelperClient.getPlayerData().getFriendListEntriesIds();
        FriendListEntry[] validEntries = new FriendListEntry[syncedIds.length];
        i = 0;
        for (long entryId : syncedIds)
            if (!friendsIds.contains(entryId))
                needSync[i++] = entryId;    
            else
                validEntries[j++] = OxygenHelperClient.getPlayerData().getFriendListEntry(entryId);
        OxygenHelperClient.getPlayerData().clearFriendListEntries();
        for (FriendListEntry validEntry : validEntries) {
            if (validEntry == null) break;
            OxygenHelperClient.getPlayerData().addFriendListEntry(validEntry);
        }
        if (i > 0)
            OxygenGUIHelper.needSync(OxygenMain.FRIEND_LIST_SCREEN_ID);
        OxygenMain.network().sendToServer(new SPSendAbsentFriendListEntriesIds(needSync, i));
    }
}
