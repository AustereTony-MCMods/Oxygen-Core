package austeretony.oxygen.client.gui.overlay;

import austeretony.alternateui.overlay.core.GUIOverlay;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.gui.settings.GUISettings;
import austeretony.oxygen.client.input.OxygenKeyHandler;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class RequestGUIOverlay {

    private final GUIOverlay overlay;

    private final GUITextLabel requestTextLabel, elapsedTimeTextLabel, acceptKeyTextLabel, acceptTextLabel, rejectKeyTextLabel, rejectTextLabel;

    private INotification notification;

    public RequestGUIOverlay() {
        this.overlay = new GUIOverlay(GUISettings.instance().getOverlayScale());
        this.overlay.addElement(this.requestTextLabel = new GUITextLabel(0, 0).setEnabledTextColor(GUISettings.instance().getBaseOverlayTextColor()));
        this.overlay.addElement(this.acceptKeyTextLabel = new GUITextLabel(0, 12).setEnabledTextColor(GUISettings.instance().getBaseOverlayTextColor()));
        this.overlay.addElement(this.acceptTextLabel = new GUITextLabel(0, 12).setEnabledTextColor(GUISettings.instance().getAdditionalOverlayTextColor()));
        this.overlay.addElement(this.rejectKeyTextLabel = new GUITextLabel(0, 22).setEnabledTextColor(GUISettings.instance().getBaseOverlayTextColor()));
        this.overlay.addElement(this.rejectTextLabel = new GUITextLabel(0, 22).setEnabledTextColor(GUISettings.instance().getAdditionalOverlayTextColor()));
        this.overlay.addElement(this.elapsedTimeTextLabel = new GUITextLabel(0, 34).setEnabledTextColor(GUISettings.instance().getAdditionalOverlayTextColor()));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)//disabling crosshair render while in GUI
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.TEXT)
            this.draw();
    }

    private void draw() {
        if (OxygenManagerClient.instance().getNotificationsManager() == null) return;
        if (OxygenManagerClient.instance().getNotificationsManager().latestRequestIdExist()) {
            if (!OxygenManagerClient.instance().getNotificationsManager().isRequestOverlayUpdated()) {
                this.notification = OxygenManagerClient.instance().getNotificationsManager().getLatestRequest();
                this.overlay.setAlignment(EnumGUIAlignment.CENTER, - 90, 30);
                this.requestTextLabel.setDisplayText(I18n.format(this.notification.getDescription(), (Object[]) this.notification.getArguments()), true);
                this.acceptKeyTextLabel.setDisplayText("[" + OxygenKeyHandler.ACCEPT.getKeyBinding().getDisplayName() + "]", true);
                this.acceptTextLabel.setX(this.requestTextLabel.textWidth(this.acceptKeyTextLabel.getDisplayText(), 1.0F) + 2);
                this.acceptTextLabel.setDisplayText(I18n.format(OxygenKeyHandler.ACCEPT.getKeyBinding().getKeyDescription()), true);
                this.rejectKeyTextLabel.setDisplayText("[" + OxygenKeyHandler.REJECT.getKeyBinding().getDisplayName() + "]", true);
                this.rejectTextLabel.setX(this.requestTextLabel.textWidth(this.rejectKeyTextLabel.getDisplayText(), 1.0F) + 2);
                this.rejectTextLabel.setDisplayText(I18n.format(OxygenKeyHandler.REJECT.getKeyBinding().getKeyDescription()), true);
                OxygenManagerClient.instance().getNotificationsManager().requestOverlayUpdated();
            }
            this.elapsedTimeTextLabel.setDisplayText("(" + this.notification.getCounter() / 20 + ")", true);
            this.overlay.draw();
        }
    }
}
