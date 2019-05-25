package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPPlaySoundEvent extends ProxyPacket {

    private int id;

    public CPPlaySoundEvent() {}

    public CPPlaySoundEvent(int id) {
        this.id = id;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.id);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenManagerClient.instance().getSoundEventsManager().playSoundAtClient(buffer.readByte());
    }
}
