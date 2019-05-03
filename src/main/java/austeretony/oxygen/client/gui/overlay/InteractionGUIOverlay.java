package austeretony.oxygen.client.gui.overlay;

import austeretony.alternateui.overlay.core.GUIOverlay;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.core.api.ClientReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InteractionGUIOverlay {

    private final GUIOverlay overlay;

    private final GUITextLabel usernameTextLabel, interactKeyTextLabel, interactKeyNameTextLabel;

    private Entity target;

    private EntityPlayer pointedPlayer;

    private boolean initialized;

    public InteractionGUIOverlay() {
        this.overlay = new GUIOverlay(GUISettings.instance().getOverlayScale());
        this.overlay.addElement(this.usernameTextLabel = new GUITextLabel(5, 0).setEnabledTextColor(GUISettings.instance().getBaseOverlayTextColor()));
        this.overlay.addElement(this.interactKeyNameTextLabel = new GUITextLabel(15, 12).setEnabledTextColor(GUISettings.instance().getBaseOverlayTextColor()));
        this.overlay.addElement(this.interactKeyTextLabel = new GUITextLabel(15, 12).setEnabledTextColor(GUISettings.instance().getAdditionalOverlayTextColor()));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (Minecraft.getMinecraft().inGameHasFocus && event.getType() == ElementType.TEXT)
            this.draw();
    }

    private void draw() {
        if ((this.target = ClientReference.getMinecraft().pointedEntity) == null) {
            this.initialized = false;
            return;
        }
        if (this.target instanceof EntityPlayer) {
            this.pointedPlayer = (EntityPlayer) this.target;
            if (!this.initialized) {
                this.overlay.setAlignment(EnumGUIAlignment.CENTER, 10, 0);
                this.usernameTextLabel.setDisplayText(this.pointedPlayer.getGameProfile().getName(), true);//not safest way to get username on client
                this.interactKeyTextLabel.setDisplayText("[" + OxygenKeyHandler.INTERACT_WITH_PLAYER.getKeyBinding().getDisplayName() + "]", true);
                this.interactKeyNameTextLabel.setX(18 + this.interactKeyTextLabel.textWidth(this.interactKeyTextLabel.getDisplayText(), 1.0F));
                this.interactKeyNameTextLabel.setDisplayText(I18n.format(OxygenKeyHandler.INTERACT_WITH_PLAYER.getKeyBinding().getKeyDescription()), true);
                this.initialized = true;
            }
            this.overlay.draw();
        }
    }
}
