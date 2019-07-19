package austeretony.oxygen.common.api;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.client.CPPlaySoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;

public class SoundEventHelperServer {

    public static void playSoundServer(EntityPlayer player, SoundEvent soundEvent) {
        player.playSound(soundEvent, 0.5F, 1.0F);
    }

    public static void playSoundServer(EntityPlayer player, SoundEvent soundEvent, float volume, float pitch) {
        player.playSound(soundEvent, volume, pitch);
    }

    public static void playSoundClient(EntityPlayer player, int id) {
        OxygenMain.network().sendTo(new CPPlaySoundEvent(id), (EntityPlayerMP) player);
    }
}
