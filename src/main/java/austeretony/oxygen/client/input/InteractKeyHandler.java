package austeretony.oxygen.client.input;

import org.lwjgl.input.Keyboard;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.interaction.InteractionHelperClient;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;

public class InteractKeyHandler {

    public static final KeyBinding INTERACT = new KeyBinding("key.oxygen.interact", Keyboard.KEY_F, "Oxygen");

    public InteractKeyHandler() {
        ClientReference.registerKeyBinding(INTERACT);
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {        
        if (INTERACT.isPressed())
            InteractionHelperClient.processInteraction();
    }
}