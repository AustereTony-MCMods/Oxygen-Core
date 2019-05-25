package austeretony.oxygen.common;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.client.CPPlaySoundEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundEvent;

public class SoundEventsManagerServer {

    private final OxygenManagerServer manager;

    public SoundEventsManagerServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void playSoundAtServer(EntityPlayer player, SoundEvent soundEvent, float volume, float pitch) {
        player.playSound(soundEvent, volume, pitch);
    }

    public void playSoundAtServer(EntityPlayer player, SoundEvent soundEvent) {
        this.playSoundAtServer(player, soundEvent, 0.5F, 1.0F);
    }

    public void playSoundAtClient(EntityPlayer player, int id) {        
        OxygenMain.network().sendTo(new CPPlaySoundEvent(id), (EntityPlayerMP) player);
    }
}
