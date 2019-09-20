package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPShowStatusMessage extends Packet {

    private int modIndex, messageIndex;

    public CPShowStatusMessage() {}

    public CPShowStatusMessage(int modIndex, int messageIndex) {
        this.modIndex = modIndex;
        this.messageIndex = messageIndex;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.modIndex);
        buffer.writeByte(this.messageIndex);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int 
        modIndex = buffer.readByte(),
        messageIndex = buffer.readByte();
        ClientReference.delegateToClientThread(()->OxygenManagerClient.instance().getChatMessagesManager().showStatusMessage(modIndex, messageIndex));
    }
}