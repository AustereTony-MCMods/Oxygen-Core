package austeretony.oxygen_core.common.sound;

import austeretony.oxygen_core.common.main.OxygenMain;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber(modid = OxygenMain.MOD_ID)
public class SoundEffects {

    private static final BiMap<Integer, SoundEvent> REGISTRY_MAP = HashBiMap.create();

    public static SoundEvent
            miscInventoryOperation,
            miscRingingCoins,

            uiNotificationReceived,
            uiButtonClick,
            uiContextOpen,
            uiContextClose,
            uiDropDownListOpen;

    @SubscribeEvent
    public static void onSoundsRegister(RegistryEvent.Register<SoundEvent> event) {
        miscInventoryOperation = addSound("misc_inventory_operation");
        miscRingingCoins = addSound("misc_ringing_coins");

        uiNotificationReceived = addSound("ui_request_received");
        uiButtonClick = addSound("ui_button_click");
        uiContextOpen = addSound("ui_context_open");
        uiContextClose = addSound("ui_context_close");
        uiDropDownListOpen = addSound("ui_drop_down_list_open");

        for (SoundEvent soundEvent : REGISTRY_MAP.values()) {
            event.getRegistry().register(soundEvent);
        }
        OxygenMain.logInfo(1, "[Core] Registered sound effects.");
    }

    private static SoundEvent addSound(String name) {
        ResourceLocation location = new ResourceLocation(OxygenMain.MOD_ID, name);
        SoundEvent soundEvent = new SoundEvent(location).setRegistryName(location);
        int id = REGISTRY_MAP.size();
        REGISTRY_MAP.put(id, soundEvent);
        return soundEvent;
    }

    @Nullable
    public static SoundEvent getSound(int id) {
        return REGISTRY_MAP.get(id);
    }

    public static int getId(SoundEvent soundEvent) {
        return REGISTRY_MAP.inverse().getOrDefault(soundEvent, -1);
    }
}
