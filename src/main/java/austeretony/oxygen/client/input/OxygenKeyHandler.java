package austeretony.oxygen.client.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.interaction.InteractionHelperClient;
import austeretony.oxygen.common.config.OxygenClientConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;

public class OxygenKeyHandler {

    public static final KeyBinding 
    ACCEPT = new KeyBinding("key.oxygen.accept", Keyboard.KEY_R, "Oxygen"),
    REJECT = new KeyBinding("key.oxygen.reject", Keyboard.KEY_X, "Oxygen");

    public OxygenKeyHandler() {
        ClientReference.registerKeyBinding(ACCEPT);
        ClientReference.registerKeyBinding(REJECT);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {        
        if (ACCEPT.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().acceptKeyPressedSynced();
        else if (REJECT.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().rejectKeyPressedSynced();
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) { 
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState())
            if (OxygenClientConfig.INTERACT_WITH_RMB.getBooleanValue())
                InteractionHelperClient.processInteraction();
    }
}