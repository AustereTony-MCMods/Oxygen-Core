package austeretony.oxygen.client.input;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
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