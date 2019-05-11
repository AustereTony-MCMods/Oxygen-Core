package austeretony.oxygen.common.main;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenSoundEffects {

    public static final SoundEvent 
    REQUEST_RECIEVED,
    BUTTON_CLICK,
    CONTEXT_OPEN,
    CONTEXT_CLOSE,
    DROP_DOWN_LIST_OPEN;

    //TODO need sound effects for GUIs opening, closing and changing sections

    static {
        ResourceLocation 
        location = new ResourceLocation(OxygenMain.MODID, "request_recieved");
        REQUEST_RECIEVED = new SoundEvent(location).setRegistryName(location);
        location = new ResourceLocation(OxygenMain.MODID, "button_click");
        BUTTON_CLICK = new SoundEvent(location).setRegistryName(location);
        location = new ResourceLocation(OxygenMain.MODID, "context_open");
        CONTEXT_OPEN = new SoundEvent(location).setRegistryName(location);       
        location = new ResourceLocation(OxygenMain.MODID, "context_close");
        CONTEXT_CLOSE = new SoundEvent(location).setRegistryName(location);
        location = new ResourceLocation(OxygenMain.MODID, "drop_down_list_open");
        DROP_DOWN_LIST_OPEN = new SoundEvent(location).setRegistryName(location);
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().registerAll(
                REQUEST_RECIEVED,
                BUTTON_CLICK,
                CONTEXT_OPEN,
                CONTEXT_CLOSE,
                DROP_DOWN_LIST_OPEN
                );
    }
}
