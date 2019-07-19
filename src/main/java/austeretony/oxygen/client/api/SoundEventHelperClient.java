package austeretony.oxygen.client.api;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import net.minecraft.util.SoundEvent;

public class SoundEventHelperClient {

    public static void playSoundClient(SoundEvent soundEvent) {
        ClientReference.getClientPlayer().playSound(soundEvent, 0.5F, 1.0F);
    }

    public static void playSoundClient(SoundEvent soundEvent, float volume, float pitch) {
        ClientReference.getClientPlayer().playSound(soundEvent, volume, pitch);
    }

    public static void playSoundClient(int id) {
        ClientReference.getClientPlayer().playSound(OxygenSoundEffects.getSoundEvent(id), 0.5F, 1.0F);
    }
}
