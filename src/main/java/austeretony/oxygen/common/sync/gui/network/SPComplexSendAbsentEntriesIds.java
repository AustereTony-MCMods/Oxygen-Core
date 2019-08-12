package austeretony.oxygen.common.sync.gui.network;

import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.network.client.CPOpenOxygenScreen;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.common.sync.gui.api.ComplexGUIHandlerServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPComplexSendAbsentEntriesIds extends ProxyPacket {

    private long[] entriesFirstIds, entriesSecondIds;

    private int screenId, amountFirst, amountSecond;

    public SPComplexSendAbsentEntriesIds() {}

    public SPComplexSendAbsentEntriesIds(int screenId, long[] entriesFirstIds, int amountFirst, long[] entriesSecondIds, int amountSecond) {
        this.screenId = screenId;

        this.entriesFirstIds = entriesFirstIds;
        this.amountFirst = amountFirst;

        this.entriesSecondIds = entriesSecondIds;
        this.amountSecond = amountSecond;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.screenId);

        buffer.writeShort(this.amountFirst);
        for (long entryId : this.entriesFirstIds) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }

        buffer.writeShort(this.amountSecond);
        for (long entryId : this.entriesSecondIds) {
            if (entryId == 0L) break;
            buffer.writeLong(entryId);
        }
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
        this.screenId = buffer.readByte();

        this.amountFirst = buffer.readShort();
        if (this.amountFirst > 0) {
            this.entriesFirstIds = new long[this.amountFirst];
            for (int i = 0; i < this.amountFirst; i++)
                this.entriesFirstIds[i] = buffer.readLong();
        }

        this.amountSecond = buffer.readShort();
        if (this.amountSecond > 0) {
            this.entriesSecondIds = new long[this.amountSecond];
            for (int i = 0; i < this.amountSecond; i++)
                this.entriesSecondIds[i] = buffer.readLong();
        }

        if (this.amountFirst != 0 || this.amountSecond != 0)
            ComplexGUIHandlerServer.getNetwork(this.screenId).sendTo(new CPComplexSyncAbsentEntries(this.screenId, playerUUID, this.entriesFirstIds, this.entriesSecondIds), playerMP);

        OxygenMain.network().sendTo(new CPOpenOxygenScreen(EnumScreenType.COMPLEX_SCREEN, this.screenId), playerMP);
        OxygenHelperServer.setSyncing(playerUUID, false);
    }
}
