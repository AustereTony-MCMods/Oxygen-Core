package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.SoundEventHelperClient;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPPlaySoundEvent extends Packet {

    private int id;

    public CPPlaySoundEvent() {}

    public CPPlaySoundEvent(int id) {
        this.id = id;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.id);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int id = buffer.readByte();
        ClientReference.delegateToClientThread(()->SoundEventHelperClient.playSoundClient(id));
    }
}