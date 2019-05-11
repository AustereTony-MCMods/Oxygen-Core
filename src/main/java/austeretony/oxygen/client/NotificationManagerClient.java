package austeretony.oxygen.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.client.event.OxygenNotificationRecievedEvent;
import austeretony.oxygen.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.core.api.ClientReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.notification.EnumNotifications;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.IOxygenNotification;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class NotificationManagerClient {

    private final OxygenManagerClient manager;

    private final Map<Long, IOxygenNotification> notifications = new ConcurrentHashMap<Long, IOxygenNotification>();

    private final Map<Integer, ResourceLocation> icons = new HashMap<Integer, ResourceLocation>(5);

    private long latestNotificationId;

    private boolean notificationsExist, requestOverlayUpdated;

    public NotificationManagerClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void openNotificationsMenu() {
        ClientReference.displayGuiScreen(new NotificationsGUIScreen());
    }

    public Map<Integer, ResourceLocation> getIcons() {
        return this.icons;
    }

    public boolean iconExist(int index) {
        return this.icons.containsKey(index);
    }

    public ResourceLocation getIcon(int index) {
        return this.icons.get(index);
    }

    public void addIcon(int index, ResourceLocation textureLocation) {
        this.icons.put(index, textureLocation);
    }

    public Map<Long, IOxygenNotification> getNotifications() {
        return this.notifications;
    }

    public IOxygenNotification getNotification(long id) {
        return this.notifications.get(id);
    }

    public void addNotification(IOxygenNotification notification) {
        this.notifications.put(notification.getId(), notification);
        if (notification.getType() == EnumNotifications.REQUEST) {
            ClientReference.getClientPlayer().playSound(OxygenSoundEffects.REQUEST_RECIEVED, 1.0F, 1.0F);//request recieved sound effect
            if (!OxygenHelperClient.getClientSettingBoolean(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING)) {
                this.latestNotificationId = notification.getId();
                this.requestOverlayUpdated = false;
            }
        }
        this.notificationsExist = true;
        MinecraftForge.EVENT_BUS.post(new OxygenNotificationRecievedEvent(notification));//custom forge event   
        if (notification.getIndex() == OxygenMain.FRIEND_REQUEST_ID 
                && OxygenHelperClient.getClientSettingBoolean(OxygenMain.FRIEND_REQUESTS_AUTO_ACCEPT_SETTING))
            notification.accepted(null);
    }

    public boolean latestRequestIdExist() {
        return this.latestNotificationId != 0L;
    }

    public long getLatestRequestId() {
        return this.latestNotificationId;
    }

    public IOxygenNotification getLatestRequest() {
        return this.notifications.get(this.latestNotificationId);
    }

    public void resetLatestRequestId() {       
        this.latestNotificationId = 0L;
    }

    public void requestOverlayUpdated() {
        this.requestOverlayUpdated = true;
    }

    public boolean isRequestOverlayUpdated() {
        return this.requestOverlayUpdated;
    }

    public void processNotifications() {
        if (this.notificationsExist) {
            Iterator<IOxygenNotification> iterator = this.notifications.values().iterator();
            IOxygenNotification notification;
            while (iterator.hasNext()) {
                notification = iterator.next();
                if (notification.isExpired()) {
                    if (notification.getId() == this.latestNotificationId)
                        this.latestNotificationId = 0L;
                    iterator.remove();
                    this.notificationsExist = this.notifications.size() > 0;
                }
            }
        }
    }

    public void acceptKeyPressedSynced() {
        if (!ClientReference.hasActiveGUI() && this.latestRequestIdExist()) {
            this.notifications.remove(this.latestNotificationId);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.ACCEPT, this.latestNotificationId));
            this.resetLatestRequestId();
        }
    }

    public void rejectKeyPressedSynced() {
        if (!ClientReference.hasActiveGUI() && this.latestRequestIdExist()) {
            this.notifications.remove(this.latestNotificationId);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.REJECT, this.latestNotificationId));
            this.resetLatestRequestId();
        }
    }
}
