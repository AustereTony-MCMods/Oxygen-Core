package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPShowStatusMessage extends Packet {

    private int modIndex, messageIndex;

    private String[] args;

    public CPShowStatusMessage() {}

    public CPShowStatusMessage(int modIndex, int messageIndex, String... args) {
        this.modIndex = modIndex;
        this.messageIndex = messageIndex;
        this.args = args;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.modIndex);
        buffer.writeByte(this.messageIndex);
        buffer.writeByte(this.args.length);
        for (String arg : this.args) 
            ByteBufUtils.writeString(arg, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int 
        modIndex = buffer.readByte(),
        messageIndex = buffer.readByte();
        final String[] args = new String[buffer.readByte()];
        for (int i = 0; i < args.length; i++)
            args[i] = ByteBufUtils.readString(buffer);
        ClientReference.delegateToClientThread(()->OxygenManagerClient.instance().getChatMessagesManager().showStatusMessage(modIndex, messageIndex, args));
    }
}