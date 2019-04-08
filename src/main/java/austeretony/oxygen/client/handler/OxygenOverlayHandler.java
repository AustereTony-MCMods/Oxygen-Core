package austeretony.oxygen.client.handler;

import austeretony.alternateui.overlay.core.GUIOverlay;
import austeretony.alternateui.screen.text.GUITextLabel;
import austeretony.alternateui.util.EnumGUIAlignment;
import austeretony.oxygen.common.notification.IOxygenNotification;
import austeretony.oxygen.common.notification.NotificationManagerClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class OxygenOverlayHandler {

    private GUIOverlay overlay;

    private GUITextLabel requestTextLabel, elapsedTimeTextLabel, acceptKeyTextLabel, acceptTextLabel, rejectKeyTextLabel, rejectTextLabel;

    private IOxygenNotification notification;

    public OxygenOverlayHandler() {
        this.overlay = new GUIOverlay(0.9F);
        this.overlay.addElement(this.requestTextLabel = new GUITextLabel(0, 0).setEnabledTextColor(0xFFAAAAAA));
        this.overlay.addElement(this.acceptKeyTextLabel = new GUITextLabel(0, 10).setEnabledTextColor(0xFFAAAAAA));
        this.overlay.addElement(this.acceptTextLabel = new GUITextLabel(0, 10));
        this.overlay.addElement(this.rejectKeyTextLabel = new GUITextLabel(0, 20).setEnabledTextColor(0xFFAAAAAA));
        this.overlay.addElement(this.rejectTextLabel = new GUITextLabel(0, 20));
        this.overlay.addElement(this.elapsedTimeTextLabel = new GUITextLabel(0, 30));
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Pre event) {
        if (event.getType() == ElementType.CROSSHAIRS)
            event.setCanceled(!Minecraft.getMinecraft().inGameHasFocus);
        if (event.getType() == ElementType.TEXT)
            this.draw();
    }

    private void draw() {
        if (NotificationManagerClient.instance() == null) return;
        if (NotificationManagerClient.instance().latestRequestIdExist()) {
            if (!NotificationManagerClient.instance().isNotificationsOverlayInitialized()) {
                this.notification = NotificationManagerClient.instance().getLatestRequest();
                this.overlay.setAlignment(EnumGUIAlignment.CENTER, - 90, 30);
                this.requestTextLabel.setDisplayText(I18n.format(this.notification.getDescription(), (Object[]) this.notification.getArguments()), true);
                this.acceptKeyTextLabel.setDisplayText("[" + OxygenKeyHandler.ACCEPT.getDisplayName() + "]", true);
                this.acceptTextLabel.setX(this.requestTextLabel.width(this.acceptKeyTextLabel.getDisplayText()) + 2);
                this.acceptTextLabel.setDisplayText(I18n.format(OxygenKeyHandler.ACCEPT.getKeyDescription()), true);
                this.rejectKeyTextLabel.setDisplayText("[" + OxygenKeyHandler.REJECT.getDisplayName() + "]", true);
                this.rejectTextLabel.setX(this.requestTextLabel.width(this.rejectKeyTextLabel.getDisplayText()) + 2);
                this.rejectTextLabel.setDisplayText(I18n.format(OxygenKeyHandler.REJECT.getKeyDescription()), true);
                NotificationManagerClient.instance().initNotificationsOverlay();
            }
            this.elapsedTimeTextLabel.setDisplayText("(" + this.notification.getCounter() / 20 + ")", true);
            this.overlay.draw();
        }
    }
}
