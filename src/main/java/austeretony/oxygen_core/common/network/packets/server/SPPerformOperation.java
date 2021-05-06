package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPPerformOperation extends Packet {

    private int handlerId, operation;
    private byte[] dataRaw;

    public SPPerformOperation() {}

    public SPPerformOperation(int handlerId, int operation, byte[] dataRaw) {
        this.handlerId = handlerId;
        this.operation = operation;
        this.dataRaw = dataRaw;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(handlerId);
        buffer.writeByte(operation);
        buffer.writeInt(dataRaw.length);
        buffer.writeBytes(dataRaw);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        final int handlerId = buffer.readShort();
        if (OxygenServer.isNetworkRequestAvailable(handlerId + 21500, MinecraftCommon.getEntityUUID(playerMP))) {
            final int operation = buffer.readByte();
            final byte[] dataRaw = new byte[buffer.readInt()];
            buffer.readBytes(dataRaw);

            OxygenServer.addTask(() -> OxygenManagerServer.instance().getNetworkOperationsManager().processOperation(playerMP,
                    handlerId, operation, dataRaw));
        }
    }
}
