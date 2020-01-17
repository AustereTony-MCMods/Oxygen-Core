package austeretony.oxygen_core.client.input;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.InteractionHelper;
import austeretony.oxygen_core.client.api.OxygenGUIHelper;
import austeretony.oxygen_core.common.config.OxygenConfig;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.MouseInputEvent;

public class OxygenKeyHandler {

    private KeyBinding notificationsKeybinding, acceptKeybinding, rejectKeybinding, interactKeybinding;

    public OxygenKeyHandler() {
        if (OxygenConfig.ENABLE_NOTIFICATIONS_KEY.asBoolean() && !OxygenGUIHelper.isOxygenMenuEnabled())
            ClientReference.registerKeyBinding(this.notificationsKeybinding = new KeyBinding("key.oxygen_core.notifications", Keyboard.KEY_N, "Oxygen"));
        if (OxygenConfig.ENABLE_ACCEPT_KEY.asBoolean())
            ClientReference.registerKeyBinding(this.acceptKeybinding = new KeyBinding("key.oxygen_core.accept", Keyboard.KEY_R, "Oxygen"));
        if (OxygenConfig.ENABLE_REJECT_KEY.asBoolean())
            ClientReference.registerKeyBinding(this.rejectKeybinding = new KeyBinding("key.oxygen_core.reject", Keyboard.KEY_X, "Oxygen"));
        if (OxygenConfig.ENABLE_INTERACTION_KEY.asBoolean())
            ClientReference.registerKeyBinding(this.interactKeybinding = new KeyBinding("key.oxygen_core.interact", Keyboard.KEY_F, "Oxygen"));
    }

    @SubscribeEvent
    public void onKeyInput(KeyInputEvent event) {        
        if (this.notificationsKeybinding != null && this.notificationsKeybinding.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().openNotificationsMenu();
        if (this.acceptKeybinding != null && this.acceptKeybinding.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().acceptRequestSynced();
        if (this.rejectKeybinding != null && this.rejectKeybinding.isPressed())
            OxygenManagerClient.instance().getNotificationsManager().rejectRequestSynced();
        if (this.interactKeybinding != null && this.interactKeybinding.isPressed())
            InteractionHelper.processInteractions();
    }

    @SubscribeEvent
    public void onMouseInput(MouseInputEvent event) { 
        if (Mouse.getEventButton() == 1 && Mouse.getEventButtonState())
            if (EnumBaseClientSetting.INTERACT_WITH_RMB.get().asBoolean())
                InteractionHelper.processInteractions();
    }

    public KeyBinding getNotificationsKeybinding() {
        return this.notificationsKeybinding;
    }

    public KeyBinding getAcceptKeybinding() {
        return this.acceptKeybinding;
    }

    public KeyBinding getRejectKeybinding() {
        return this.rejectKeybinding;
    }

    public KeyBinding getInteractionKeybinding() {
        return this.interactKeybinding;
    }
}
