package austeretony.oxygen_core.client.api;

import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraft.util.SoundEvent;

public class SoundEventHelperClient {

    public static void playSoundClient(SoundEvent soundEvent, float volume, float pitch) {
        if (EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean())
            ClientReference.getClientPlayer().playSound(soundEvent, volume, pitch);
    }

    public static void playSoundClient(SoundEvent soundEvent) {
        playSoundClient(soundEvent, 0.4F, 1.0F);
    }

    public static void playSoundClient(int id) {
        playSoundClient(OxygenSoundEffects.getSoundEvent(id));
    }
}
