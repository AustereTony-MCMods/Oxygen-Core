package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

import java.util.UUID;

public class CPSendRequest extends Packet {

    private String message;
    private String[] args;
    private UUID requestUUID;

    public CPSendRequest() {}

    public CPSendRequest(String message, String[] args, UUID requestUUD) {
        this.message = message;
        this.args = args;
        this.requestUUID = requestUUD;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        ByteBufUtils.writeString(message, buffer);
        buffer.writeByte(args.length);
        for (String arg : args) {
            ByteBufUtils.writeString(arg, buffer);
        }
        ByteBufUtils.writeUUID(requestUUID, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final String message = ByteBufUtils.readString(buffer);
        final String[] args = new String[buffer.readByte()];
        for (int i = 0; i < args.length; i++) {
            args[i] = ByteBufUtils.readString(buffer);
        }
        final UUID requestUUID = ByteBufUtils.readUUID(buffer);
        MinecraftClient.delegateToClientThread(
                () -> OxygenManagerClient.instance().getNotificationsManager().addRequest(message, args, requestUUID));
    }
}
