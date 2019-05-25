package austeretony.oxygen.common.api;

import austeretony.oxygen.common.OxygenManagerServer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.SoundEvent;

public class SoundEventsHelperServer {

    public static void playSoundServer(EntityPlayer player, SoundEvent soundEvent) {
        OxygenManagerServer.instance().getSoundEventsManager().playSoundAtServer(player, soundEvent);
    }

    public static void playSoundServer(EntityPlayer player, SoundEvent soundEvent, float volume, float pitch) {
        OxygenManagerServer.instance().getSoundEventsManager().playSoundAtServer(player, soundEvent, volume, pitch);
    }

    public static void playSoundClient(EntityPlayer player, int id) {
        OxygenManagerServer.instance().getSoundEventsManager().playSoundAtClient(player, id);
    }
}
