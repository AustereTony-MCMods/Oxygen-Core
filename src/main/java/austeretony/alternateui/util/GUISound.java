package austeretony.alternateui.util;

/**
 * Упакованный звук для элементов ГПИ.
 * 
 * @author AustereTony
 */
public class GUISound {

    public final String name;

    public final float volume, pitch;

    /**
     * Звук для элементов ГПИ.
     * 
     * @param location путь к файлу
     * @param volume
     * @param pitch
     */
    public GUISound(String location, float volume, float pitch) {

        this.name = location;
        this.volume = volume;
        this.pitch = pitch;
    }
}
