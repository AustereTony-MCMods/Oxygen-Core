package austeretony.oxygen.client.handler;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class OxygenKeyHandler {

    public static final KeyBindingWrapper 
    NOTIFICATIONS_MENU = new KeyBindingWrapper(),
    ACCEPT = new KeyBindingWrapper(),
    REJECT = new KeyBindingWrapper(),
    FRIENDS_LIST = new KeyBindingWrapper();

    public OxygenKeyHandler() {
        NOTIFICATIONS_MENU.register("key.oxygen.notifications", Keyboard.KEY_N, OxygenMain.NAME);
        ACCEPT.register("key.oxygen.accept", Keyboard.KEY_R, OxygenMain.NAME);
        REJECT.register("key.oxygen.reject", Keyboard.KEY_X, OxygenMain.NAME);
        if (OxygenConfig.ENABLE_FRIENDS_LIST.getBooleanValue())
            FRIENDS_LIST.register("key.oxygen.friends", Keyboard.KEY_U, OxygenMain.NAME);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {
        if (NOTIFICATIONS_MENU.registered() && NOTIFICATIONS_MENU.getKeyBinding().isPressed())
            OxygenManagerClient.instance().getNotificationsManager().openNotificationsMenu();
        else if (ACCEPT.registered() && ACCEPT.getKeyBinding().isPressed())
            OxygenManagerClient.instance().getNotificationsManager().acceptKeyPressedSynced();
        else if (REJECT.registered() && REJECT.getKeyBinding().isPressed())
            OxygenManagerClient.instance().getNotificationsManager().rejectKeyPressedSynced();
        else if (FRIENDS_LIST.registered() && FRIENDS_LIST.getKeyBinding().isPressed())
            OxygenManagerClient.instance().getFriendListManager().openFriendsListSynced();
    }
}
