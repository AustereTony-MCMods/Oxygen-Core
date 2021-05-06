package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.chat.StatusMessageType;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPSendStatusMessage extends Packet {

    private int modIndex, typeOrdinal;
    private String message;
    private String[] args;

    public CPSendStatusMessage() {}

    public CPSendStatusMessage(int modIndex, StatusMessageType type, String message, String[] args) {
        this.modIndex = modIndex;
        this.typeOrdinal = type.ordinal();
        this.message = message;
        this.args = args;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeShort(modIndex);
        buffer.writeByte(typeOrdinal);
        ByteBufUtils.writeString(message, buffer);

        buffer.writeByte(args.length);
        for (String arg : args) {
            ByteBufUtils.writeString(arg, buffer);
        }
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int modIndex = buffer.readShort();
        final StatusMessageType type = StatusMessageType.values()[buffer.readByte()];
        final String message = ByteBufUtils.readString(buffer);

        final String[] args = new String[buffer.readByte()];
        for (int i = 0; i < args.length; i++) {
            args[i] = ByteBufUtils.readString(buffer);
        }

        MinecraftClient.delegateToClientThread(() ->
                OxygenManagerClient.instance().showStatusMessage(modIndex, type, message, args));
    }
}
