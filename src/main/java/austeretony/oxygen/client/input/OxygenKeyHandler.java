package austeretony.oxygen.client.input;

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
    FRIENDS_LIST = new KeyBindingWrapper(),
    PLAYERS_LIST = new KeyBindingWrapper(),
    INTERACT_WITH_PLAYER = new KeyBindingWrapper();

    public OxygenKeyHandler() {
        NOTIFICATIONS_MENU.register("key.oxygen.notifications", Keyboard.KEY_N, OxygenMain.NAME);
        ACCEPT.register("key.oxygen.accept", Keyboard.KEY_R, OxygenMain.NAME);
        REJECT.register("key.oxygen.reject", Keyboard.KEY_X, OxygenMain.NAME);
        if (OxygenConfig.ENABLE_FRIENDS_LIST.getBooleanValue())
            FRIENDS_LIST.register("key.oxygen.friends", Keyboard.KEY_O, OxygenMain.NAME);
        if (OxygenConfig.REPLACE_TAB_OVERLAY.getBooleanValue())
            PLAYERS_LIST.register("key.oxygen.playersOnline", Keyboard.KEY_TAB, OxygenMain.NAME);
        INTERACT_WITH_PLAYER.register("key.oxygen.iteractWithPlayer", Keyboard.KEY_F, OxygenMain.NAME);
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
        else if (PLAYERS_LIST.registered() && PLAYERS_LIST.getKeyBinding().isPressed())
            OxygenManagerClient.instance().openPlayersListSynced();
        else if (INTERACT_WITH_PLAYER.registered() && INTERACT_WITH_PLAYER.getKeyBinding().isPressed())
            OxygenManagerClient.instance().getInteractionManager().openPlayerInteractionMenuSynced();
    }
}
