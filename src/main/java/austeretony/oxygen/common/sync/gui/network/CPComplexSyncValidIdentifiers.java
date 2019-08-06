package austeretony.oxygen.common.sync.gui.network;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.client.api.OxygenGUIHelper;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.sync.gui.api.ComplexGUIHandlerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.api.ComplexGUIHandlerServer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPComplexSyncValidIdentifiers extends ProxyPacket {

    private int screenId;

    private UUID playerUUID;

    public CPComplexSyncValidIdentifiers() {}

    public CPComplexSyncValidIdentifiers(int screenId, UUID playerUUID) {
        this.screenId = screenId;
        this.playerUUID = playerUUID;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);

        Set<Long> entriesIds = ComplexGUIHandlerServer.getValidIdentifiersFirst(this.screenId, this.playerUUID);
        buffer.writeShort(entriesIds.size());
        for (long entryId : entriesIds)
            buffer.writeLong(entryId);

        entriesIds = ComplexGUIHandlerServer.getValidIdentifiersSecond(this.screenId, this.playerUUID);
        buffer.writeShort(entriesIds.size());
        for (long entryId : entriesIds)
            buffer.writeLong(entryId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        this.screenId = buffer.readByte();
        int 
        i = 0, 
        j = 0,
        k = 0;

        long[] syncedIds = new long[buffer.readShort()];
        for (; i < syncedIds.length; i++)
            syncedIds[i] = buffer.readLong();
        long[] needSyncFirst = new long[syncedIds.length];
        Set<Long> clientIds = ComplexGUIHandlerClient.getIdentifiersFirst(this.screenId);
        Object[] validEntries = new Object[syncedIds.length];
        i = 0;
        for (long entryId : syncedIds)
            if (!clientIds.contains(entryId))
                needSyncFirst[i++] = entryId;    
            else
                validEntries[j++] = ComplexGUIHandlerClient.getEntryFirst(this.screenId, entryId);
        ComplexGUIHandlerClient.clearDataFirst(this.screenId);
        for (Object validEntry : validEntries) {
            if (validEntry == null) break;
            ComplexGUIHandlerClient.addValidEntryFirst(this.screenId, validEntry);
        }

        syncedIds = new long[buffer.readShort()];
        for (; k < syncedIds.length; k++)
            syncedIds[k] = buffer.readLong();
        long[] needSyncSecond = new long[syncedIds.length];
        clientIds = ComplexGUIHandlerClient.getIdentifiersSecond(this.screenId);
        validEntries = new Object[syncedIds.length];
        k = j = 0;
        for (long entryId : syncedIds)
            if (!clientIds.contains(entryId))
                needSyncSecond[k++] = entryId;    
            else
                validEntries[j++] = ComplexGUIHandlerClient.getEntrySecond(this.screenId, entryId);
        ComplexGUIHandlerClient.clearDataSecond(this.screenId);
        for (Object validEntry : validEntries) {
            if (validEntry == null) break;
            ComplexGUIHandlerClient.addValidEntrySecond(this.screenId, validEntry);
        }

        if (ClientReference.getCurrentScreen() != null)
            ClientReference.displayGuiScreen(null);
        OxygenGUIHelper.needSync(this.screenId);

        if (i == 0 
                && k == 0)
            OxygenGUIHelper.dataReceived(this.screenId);

        ComplexGUIHandlerClient.getNetwork(this.screenId).sendToServer(new SPComplexSendAbsentEntriesIds(this.screenId, needSyncFirst, i, needSyncSecond, k));
    }
}
