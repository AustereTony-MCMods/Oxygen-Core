package austeretony.oxygen.common.network.client;

import java.util.UUID;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.SharedPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncSharedPlayersData extends ProxyPacket {

    private int[] ids;

    private boolean opped;

    public CPSyncSharedPlayersData() {}

    public CPSyncSharedPlayersData(boolean opped, int... identifiers) {
        this.opped = opped;
        this.ids = identifiers;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.ids.length);
        for (int id : this.ids)
            buffer.writeByte(id);
        buffer.writeShort(OxygenManagerServer.instance().getSharedPlayersData().size());
        for (SharedPlayerData playerData : OxygenManagerServer.instance().getSharedPlayersData()) {
            PacketBufferUtils.writeUUID(playerData.getPlayerUUID(), buffer);
            playerData.write(buffer, this.ids);
        }
        buffer.writeBoolean(this.opped);
    }    

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int i;
        this.ids = new int[buffer.readByte()];
        for (i = 0; i < this.ids.length; i++)
            this.ids[i] = buffer.readByte();
        int size = buffer.readShort();
        SharedPlayerData playerData;
        UUID playerUUID;
        OxygenManagerClient.instance().getOnlinePlayers().clear();
        for (i = 0; i < size; i++) {
            playerUUID = PacketBufferUtils.readUUID(buffer);
            OxygenManagerClient.instance().getOnlinePlayers().add(playerUUID);
            if (!OxygenManagerClient.instance().getSharedPlayersDataUUIDs().contains(playerUUID)) {
                playerData = new SharedPlayerData();
                playerData.setUUID(playerUUID);
                playerData.read(buffer, this.ids);
                OxygenManagerClient.instance().addSharedPlayerData(playerData);
            } else
                OxygenManagerClient.instance().getSharedPlayerData(playerUUID).read(buffer, this.ids);
        }
        OxygenHelperClient.getPlayerData().setOpped(buffer.readBoolean());
    }
}
