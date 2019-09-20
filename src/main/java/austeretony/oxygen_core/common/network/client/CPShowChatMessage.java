package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.event.OxygenChatMessageEvent;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;
import net.minecraftforge.common.MinecraftForge;

public class CPShowChatMessage extends Packet {

    private int modIndex, messageIndex;

    private String[] args;

    public CPShowChatMessage() {}

    public CPShowChatMessage(int modIndex, int messageIndex, String... args) {
        this.modIndex = modIndex;
        this.messageIndex = messageIndex;
        this.args = args;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.modIndex);
        buffer.writeByte(this.messageIndex);
        buffer.writeByte(this.args.length);
        if (this.args.length > 0)
            for (String arg : this.args)
                ByteBufUtils.writeString(arg, buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int modIndex = buffer.readByte();
        final int messageIndex = buffer.readByte();
        final String[] args = new String[buffer.readByte()];
        if (args.length > 0)
            for (int i = 0; i < args.length; i++)
                args[i] = ByteBufUtils.readString(buffer);
        ClientReference.delegateToClientThread(()->MinecraftForge.EVENT_BUS.post(new OxygenChatMessageEvent(modIndex, messageIndex, args)));
    }
}
