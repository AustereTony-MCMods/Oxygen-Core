package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPPerformOperation extends Packet {

    private int handlerId, operation;
    private byte[] dataRaw;

    public CPPerformOperation() {}

    public CPPerformOperation(int handlerId, int operation, byte[] dataRaw) {
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
        final int handlerId = buffer.readShort();
        final int operation = buffer.readByte();
        final byte[] dataRaw = new byte[buffer.readInt()];
        buffer.readBytes(dataRaw);
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getOperationsManager().processOperation(handlerId, operation, dataRaw));
    }
}
