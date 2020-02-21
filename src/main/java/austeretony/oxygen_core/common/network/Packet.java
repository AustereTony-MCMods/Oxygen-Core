package austeretony.oxygen_core.common.network;

import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetHandlerPlayServer;

public abstract class Packet {

    public static Packet create(Class<? extends Packet> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception exception) {
            OxygenMain.LOGGER.error("Oxygen Network error. Failed to create packet of <{}> class.", clazz.getCanonicalName());
        }
        return null;
    }

    public abstract void write(ByteBuf buffer, INetHandler netHandler);

    public abstract void read(ByteBuf buffer, INetHandler netHandler);

    public static EntityPlayerMP getEntityPlayerMP(INetHandler netHandler) {
        return ((NetHandlerPlayServer) netHandler).player;
    }
}
