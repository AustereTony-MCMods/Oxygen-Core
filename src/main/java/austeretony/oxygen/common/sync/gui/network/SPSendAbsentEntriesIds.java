package austeretony.oxygen.common.sync.gui.network;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.client.CPOpenOxygenScreen;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.common.sync.gui.api.AdvancedGUIHandlerServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPSendAbsentEntriesIds extends ProxyPacket {

    private long[] entriesIds;

    private int screenId, amount;

    public SPSendAbsentEntriesIds() {}

    public SPSendAbsentEntriesIds(int screenId, long[] entriesIds, int amount) {
        this.screenId = screenId;
        this.entriesIds = entriesIds;
        this.amount = amount;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);
        buffer.writeShort(this.amount);
        for (long entryId : this.entriesIds) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        int 
        screenId = buffer.readByte(),
        amount = buffer.readShort();
        if (amount > 0) {
            long[] needSync = new long[amount];
            for (int i = 0; i < amount; i++)
                needSync[i] = buffer.readLong();
            AdvancedGUIHandlerServer.getNetwork(screenId).sendTo(new CPSyncAbsentEntries(screenId, playerUUID, needSync), playerMP);
        }
        OxygenMain.network().sendTo(new CPOpenOxygenScreen(EnumScreenType.ADVANCED_SCREEN, screenId), playerMP);
        OxygenHelperServer.setSyncing(playerUUID, false);
    }
}
