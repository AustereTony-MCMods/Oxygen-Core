package austeretony.oxygen_core.common.network.packets.client;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.sound.SoundEffects;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;
import net.minecraft.util.SoundEvent;

public class CPPlaySoundAtPlayer extends Packet {

    private int id;

    public CPPlaySoundAtPlayer() {}

    public CPPlaySoundAtPlayer(int id) {
        this.id = id;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(id);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final int id = buffer.readByte();
        MinecraftClient.delegateToClientThread(() -> {
            SoundEvent sound = SoundEffects.getSound(id);
            if (sound != null) {
                MinecraftClient.playSound(sound);
            }
        });
    }
}
