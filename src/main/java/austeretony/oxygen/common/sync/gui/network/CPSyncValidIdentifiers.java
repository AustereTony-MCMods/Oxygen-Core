package austeretony.oxygen.common.sync.gui.network;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.sync.gui.api.AdvancedGUIHandlerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.api.AdvancedGUIHandlerServer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncValidIdentifiers extends ProxyPacket {

    private int screenId;

    private UUID playerUUID;

    public CPSyncValidIdentifiers() {}

    public CPSyncValidIdentifiers(int screenId, UUID playerUUID) {
        this.screenId = screenId;
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);
        Set<Long> entriesIds = AdvancedGUIHandlerServer.getValidIdentifiers(this.screenId, this.playerUUID);
        buffer.writeShort(entriesIds.size());
        for (long entryId : entriesIds)
            buffer.writeLong(entryId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int 
        screenId = buffer.readByte(),
        i = 0, 
        j = 0;
        long[] syncedIds = new long[buffer.readShort()];
        for (; i < syncedIds.length; i++)
            syncedIds[i] = buffer.readLong();
        long[] needSync = new long[syncedIds.length];
        Set<Long> clientIds = AdvancedGUIHandlerClient.getIdentifiers(screenId);
        Object[] validEntries = new Object[syncedIds.length];
        i = 0;
        for (long entryId : syncedIds)
            if (!clientIds.contains(entryId))
                needSync[i++] = entryId;    
            else
                validEntries[j++] = AdvancedGUIHandlerClient.getEntry(screenId, entryId);
        AdvancedGUIHandlerClient.clearData(screenId);
        for (Object validEntry : validEntries) {
            if (validEntry == null) break;
            AdvancedGUIHandlerClient.addValidEntry(screenId, validEntry);
        }
        if (ClientReference.getCurrentScreen() != null)
            ClientReference.displayGuiScreen(null);
        OxygenGUIHelper.needSync(screenId);
        if (i == 0)
            OxygenGUIHelper.dataReceived(screenId);
        AdvancedGUIHandlerClient.getNetwork(screenId).sendToServer(new SPSendAbsentEntriesIds(screenId, needSync, i));
    }
}
