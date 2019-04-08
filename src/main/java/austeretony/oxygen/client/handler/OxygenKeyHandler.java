package austeretony.oxygen.client.handler;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen.client.reference.ClientReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class OxygenKeyHandler {

    public static final KeyBinding 
    NOTIFICATIONS_MENU = registerKeyBinding("key.oxygen.notifications", Keyboard.KEY_N, OxygenMain.NAME),
    ACCEPT = registerKeyBinding("key.oxygen.accept", Keyboard.KEY_R, OxygenMain.NAME),
    REJECT = registerKeyBinding("key.oxygen.reject", Keyboard.KEY_X, OxygenMain.NAME);

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (NOTIFICATIONS_MENU.isPressed())
            OxygenManagerClient.instance().openNotificationsMenu();
        else if (ACCEPT.isPressed())
            NotificationManagerClient.instance().acceptKeyPressedSynced();
        else if (REJECT.isPressed())
            NotificationManagerClient.instance().rejectKeyPressedSynced();
    }

    public static KeyBinding registerKeyBinding(String name, int keyCode, String category) {
        KeyBinding keyBinding = new KeyBinding(name, keyCode, category);
        ClientReference.registerKeyBinding(keyBinding);
        return keyBinding;
    }
}
