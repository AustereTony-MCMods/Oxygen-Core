package austeretony.oxygen_core.server.api;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPPlaySoundEvent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;

public class SoundEventHelperServer {

    public static void playSoundServer(EntityPlayerMP playerMP, SoundEvent soundEvent, float volume, float pitch) {
        playerMP.playSound(soundEvent, volume, pitch);
    }

    public static void playSoundServer(EntityPlayerMP playerMP, SoundEvent soundEvent) {
        playSoundServer(playerMP, soundEvent, 0.5F, 1.0F);
    }

    public static void playSoundClient(EntityPlayerMP playerMP, int id) {
        OxygenMain.network().sendTo(new CPPlaySoundEvent(id), playerMP);
    }
}
