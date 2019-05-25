package austeretony.oxygen.client;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import net.minecraft.util.SoundEvent;

public class SoundEventsManagerClient {

    private final OxygenManagerClient manager;

    public SoundEventsManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void playSoundAtClient(SoundEvent soundEvent, float volume, float pitch) {
        ClientReference.getClientPlayer().playSound(soundEvent, volume, pitch);
    }
    
    public void playSoundAtClient(SoundEvent soundEvent) {
        this.playSoundAtClient(soundEvent, 0.5F, 1.0F);
    }

    public void playSoundAtClient(int id) {
        ClientReference.getClientPlayer().playSound(OxygenSoundEffects.getSoundEvent(id), 0.5F, 1.0F);
    }
}
