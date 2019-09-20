package austeretony.oxygen_core.common.network;

import com.google.common.collect.HashBiMap;

import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

public abstract class Packet {

    public static Packet create(HashBiMap<Integer, Class<? extends Packet>> packets, int id) {
        try {
            return packets.get(id).newInstance();
        } catch (Exception exception) {
            OxygenMain.LOGGER.error("Oxygen Network error. Failed to create packet with id <{}>", id);
        }
        return null;
    }

    public abstract void write(ByteBuf buffer, INetHandler netHandler);

    public abstract void read(ByteBuf buffer, INetHandler netHandler);

    public static EntityPlayerMP getEntityPlayerMP(INetHandler netHandler) {
        return ((NetHandlerPlayServer) netHandler).player;
    }
}