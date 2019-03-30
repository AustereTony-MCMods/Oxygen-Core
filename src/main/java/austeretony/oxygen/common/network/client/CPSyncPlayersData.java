package austeretony.oxygen.common.network.client;

import java.util.Map;
import java.util.UUID;

import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncPlayersData extends ProxyPacket {

    private int[] ids;

    public CPSyncPlayersData() {}

    public CPSyncPlayersData(int... identifiers) {
        this.ids = identifiers;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.ids.length);
        for (int id : this.ids)
            buffer.writeByte(id);
        buffer.writeShort(OxygenManagerServer.instance().getPlayersData().size());
        for (Map.Entry<UUID, OxygenPlayerData> entry : OxygenManagerServer.instance().getPlayersData().entrySet()) {
            buffer.writeLong(entry.getKey().getMostSignificantBits());
            buffer.writeLong(entry.getKey().getLeastSignificantBits());
            entry.getValue().write(buffer, this.ids);
        }
    }    

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int i;
        this.ids = new int[buffer.readByte()];
        for (i = 0; i < this.ids.length; i++)
            this.ids[i] = buffer.readByte();
        int size = buffer.readShort();
        OxygenPlayerData playerData;
        UUID playerUUID;
        OxygenManagerClient.instance().getOnlinePlayers().clear();
        for (i = 0; i < size; i++) {
            playerUUID = new UUID(buffer.readLong(), buffer.readLong());
            OxygenManagerClient.instance().getOnlinePlayers().add(playerUUID);
            if (!OxygenManagerClient.instance().getPlayersData().containsKey(playerUUID)) {
                playerData = new OxygenPlayerData();
                playerData.setUUID(playerUUID);
                playerData.read(buffer, this.ids);
                OxygenManagerClient.instance().getPlayersData().put(playerData.getUUID(), playerData);
            } else
                OxygenManagerClient.instance().getPlayerData(playerUUID).read(buffer, this.ids);
        }
    }
}
