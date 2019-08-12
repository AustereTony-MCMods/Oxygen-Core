package austeretony.oxygen.common.sync.gui.network;

import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncSharedData extends ProxyPacket {

    private int[] ids;

    public CPSyncSharedData() {}

    public CPSyncSharedData(int... identifiers) {
        this.ids = identifiers;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        int valid = 0;
        for (; valid < this.ids.length; valid++) 
            if (this.ids[valid] == 0)
                break;
        buffer.writeByte(valid);
        for (int i = 0; i < valid; i++)
            buffer.writeByte(this.ids[i]);

        buffer.writeShort(OxygenManagerServer.instance().getSharedDataManager().getOnlinePlayersIndexes().size());
        for (SharedPlayerData sharedData : OxygenManagerServer.instance().getSharedDataManager().getPlayersSharedData()) {
            if (OxygenHelperServer.isOnline(sharedData.getIndex())) {
                buffer.writeInt(sharedData.getIndex());
                sharedData.write(buffer, valid, this.ids);   
            }
        }
    }    

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int i = 0;
        this.ids = new int[buffer.readByte()];
        for (; i < this.ids.length; i++)
            this.ids[i] = buffer.readByte();

        int 
        size = buffer.readShort(),
        index;
        for (i = 0; i < size; i++) {
            index = buffer.readInt();
            OxygenHelperClient.getSharedPlayerData(index).read(buffer, this.ids);
        }
    }
}
