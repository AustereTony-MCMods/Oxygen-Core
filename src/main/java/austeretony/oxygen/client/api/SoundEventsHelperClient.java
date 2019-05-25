package austeretony.oxygen.client.api;

import austeretony.oxygen.client.OxygenManagerClient;
import net.minecraft.util.SoundEvent;

public class SoundEventsHelperClient {

    public static void playSoundClient(SoundEvent soundEvent) {
        OxygenManagerClient.instance().getSoundEventsManager().playSoundAtClient(soundEvent);
    }

    public static void playSoundClient(SoundEvent soundEvent, float volume, float pitch) {
        OxygenManagerClient.instance().getSoundEventsManager().playSoundAtClient(soundEvent, volume, pitch);
    }
}
