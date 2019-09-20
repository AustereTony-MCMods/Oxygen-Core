package austeretony.oxygen_core.client.input;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class NotificationsMenuKeyHandler {

    public static final KeyBinding NOTIFICATIONS_MENU = new KeyBinding("key.oxygen.notifications", Keyboard.KEY_N, "Oxygen");

    public NotificationsMenuKeyHandler() {
        ClientReference.registerKeyBinding(NOTIFICATIONS_MENU);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {        
        if (NOTIFICATIONS_MENU.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().openNotificationsMenu();
    }
}
