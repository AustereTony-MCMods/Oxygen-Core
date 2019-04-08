package austeretony.oxygen.common.network.client;

import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.util.PacketBufferUtils;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPShowMessage extends ProxyPacket {

    private int modIndex, messageIndex;

    private String[] args;

    public CPShowMessage() {}

    public CPShowMessage(int modIndex, int messageIndex, String... args) {
        this.modIndex = modIndex;
        this.messageIndex = messageIndex;
        this.args = args;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.modIndex);
        buffer.writeByte(this.messageIndex);
        buffer.writeByte(this.args.length);
        if (this.args.length > 0)
            for (String arg : this.args)
                PacketBufferUtils.writeString(arg, buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        this.modIndex = buffer.readByte();
        this.messageIndex = buffer.readByte();
        this.args = new String[buffer.readByte()];
        if (this.args.length > 0)
            for (int i = 0; i < this.args.length; i++)
                this.args[i] = PacketBufferUtils.readString(buffer);
        OxygenManagerClient.instance().notifyChatMessageInfoListeners(this.modIndex, this.messageIndex, this.args);
    }
}
