package austeretony.alternateui.util;

import net.minecraft.util.SoundEvent;

/**
 * Packed sound effect for GUI.
 * 
 * @author AustereTony
 */
public class GUISoundEffect {

    public final SoundEvent sound;

    public final float volume, pitch;

    public GUISoundEffect(SoundEvent sound, float volume, float pitch) {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }
}
