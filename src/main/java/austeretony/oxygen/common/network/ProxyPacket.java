package austeretony.oxygen.common.network;

import com.google.common.collect.HashBiMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.PacketBuffer;

public abstract class ProxyPacket {

    public static ProxyPacket create(HashBiMap<Integer, Class<? extends ProxyPacket>> packets, int id) {
        try {
            Class<? extends ProxyPacket> packetClass = packets.get(id);
            return packetClass == null ? null : packetClass.newInstance();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public abstract void write(PacketBuffer buffer, INetHandler netHandler);

    public abstract void read(PacketBuffer buffer, INetHandler netHandler);

    public static EntityPlayerMP getEntityPlayerMP(INetHandler netHandler) {
        return ((NetHandlerPlayServer) netHandler).player;
    }
}
