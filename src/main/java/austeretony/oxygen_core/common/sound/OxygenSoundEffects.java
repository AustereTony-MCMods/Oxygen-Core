package austeretony.oxygen_core.common.sound;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.common.main.OxygenMain;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenSoundEffects {

    private static final Map<Integer, SoundEventContainer> REGISTRY = new HashMap<>(7);

    public static final SoundEventContainer 
    INVENTORY_OPERATION = new SoundEventContainer(OxygenMain.MODID, "inventory_operation"),
    RINGING_COINS = new SoundEventContainer(OxygenMain.MODID, "ringing_coins"),
    NOTIFICATION_RECEIVED = new SoundEventContainer(OxygenMain.MODID, "request_recieved"),
    BUTTON_CLICK = new SoundEventContainer(OxygenMain.MODID, "button_click"),
    CONTEXT_OPEN = new SoundEventContainer(OxygenMain.MODID, "context_open"),
    CONTEXT_CLOSE = new SoundEventContainer(OxygenMain.MODID, "context_close"),
    DROP_DOWN_LIST_OPEN = new SoundEventContainer(OxygenMain.MODID, "drop_down_list_open");

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                INVENTORY_OPERATION.getSoundEvent(),
                RINGING_COINS.getSoundEvent(),
                NOTIFICATION_RECEIVED.getSoundEvent(),
                BUTTON_CLICK.getSoundEvent(),
                CONTEXT_OPEN.getSoundEvent(),
                CONTEXT_CLOSE.getSoundEvent(),
                DROP_DOWN_LIST_OPEN.getSoundEvent());
    }

    public static SoundEvent getSoundEvent(int id) {
        return REGISTRY.get(id).getSoundEvent();
    }

    public static class SoundEventContainer {

        private final SoundEvent soundEvent;

        private final int id;

        public SoundEventContainer(String modId, String name) {
            ResourceLocation location = new ResourceLocation(modId, name);
            this.soundEvent = new SoundEvent(location).setRegistryName(location);
            this.id = REGISTRY.size();
            REGISTRY.put(this.id, this);
        }

        public SoundEvent getSoundEvent() {
            return this.soundEvent;
        }

        public int getId() {
            return this.id;
        }
    }
}
