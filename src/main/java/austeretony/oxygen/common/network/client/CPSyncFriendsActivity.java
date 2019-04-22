package austeretony.oxygen.common.network.client;

import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.main.FriendListEntry;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncFriendsActivity extends ProxyPacket {

    private OxygenPlayerData playerData;

    public CPSyncFriendsActivity() {}

    public CPSyncFriendsActivity(OxygenPlayerData playerData) {
        this.playerData = playerData;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeShort(this.playerData.getFriendsAmount());
        for (FriendListEntry entry : this.playerData.getFriendListEntries()) {
            if (!entry.ignored) {
                buffer.writeLong(entry.getId());
                buffer.writeLong(entry.getLastActivityTime());
            }
        }
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        int amount = buffer.readShort();
        for (int i = 0; i < amount; i++)
            OxygenHelperClient.getPlayerData().getFriendListEntry(buffer.readLong()).setLastActivityTime(buffer.readLong());
    }
}
