package austeretony.oxygen.client.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.interaction.InteractionHelperClient;
import austeretony.oxygen.common.config.OxygenConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;

public class OxygenKeyHandler {

    public static final KeyBinding 
    NOTIFICATIONS_MENU = new KeyBinding("key.oxygen.notifications", Keyboard.KEY_N, "Oxygen"),
    ACCEPT = new KeyBinding("key.oxygen.accept", Keyboard.KEY_R, "Oxygen"),
    REJECT = new KeyBinding("key.oxygen.reject", Keyboard.KEY_X, "Oxygen"),
    INTERACT = new KeyBinding("key.oxygen.interact", Keyboard.KEY_F, "Oxygen");

    public OxygenKeyHandler() {
        ClientReference.registerKeyBinding(NOTIFICATIONS_MENU);
        ClientReference.registerKeyBinding(ACCEPT);
        ClientReference.registerKeyBinding(REJECT);
        ClientReference.registerKeyBinding(INTERACT);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {        
        if (NOTIFICATIONS_MENU.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().openNotificationsMenu();
        else if (ACCEPT.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().acceptKeyPressedSynced();
        else if (REJECT.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().rejectKeyPressedSynced();
        else if (INTERACT.isPressed() && !OxygenConfig.INTERACT_WITH_RMB.getBooleanValue())
            InteractionHelperClient.processInteraction();
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) { 
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState())
            if (OxygenConfig.INTERACT_WITH_RMB.getBooleanValue())
                InteractionHelperClient.processInteraction();
    }
}
