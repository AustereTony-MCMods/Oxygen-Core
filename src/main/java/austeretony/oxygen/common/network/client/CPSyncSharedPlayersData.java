package austeretony.oxygen.common.network.client;

import java.util.Collection;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncSharedPlayersData extends ProxyPacket {

    private int[] ids;

    public CPSyncSharedPlayersData() {}

    public CPSyncSharedPlayersData(int... identifiers) {
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

        Collection<SharedPlayerData> data = OxygenManagerServer.instance().getSharedPlayersData();
        buffer.writeShort(data.size());
        for (SharedPlayerData sharedData : data) {
            buffer.writeShort(sharedData.getIndex());
            sharedData.write(buffer, valid, this.ids);   
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
            index = buffer.readShort();
            OxygenManagerClient.instance().getSharedPlayerData(index).read(buffer, this.ids);
        }

        OxygenManagerClient.instance().getGUIManager().updateSharedDataListenersDataState(true);
    }
}
