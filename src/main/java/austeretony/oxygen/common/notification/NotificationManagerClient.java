package austeretony.oxygen.common.notification;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.client.reference.ClientReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPRequestReply;
import net.minecraft.util.ResourceLocation;

public class NotificationManagerClient {

    private static NotificationManagerClient instance;

    private final Map<Long, IOxygenNotification> notifications = new ConcurrentHashMap<Long, IOxygenNotification>();

    private final Map<Integer, ResourceLocation> icons = new HashMap<Integer, ResourceLocation>();

    private long latestNotificationId;

    private boolean notificationsExist, notificationsOverlayInitialized;

    private NotificationManagerClient() {}

    public static void create() {
        instance = new NotificationManagerClient();
    }

    public static NotificationManagerClient instance() {
        return instance;
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
            this.latestNotificationId = notification.getId();
            this.notificationsOverlayInitialized = false;
        }
        this.notificationsExist = true;
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

    public void initNotificationsOverlay() {
        this.notificationsOverlayInitialized = true;
    }

    public boolean isNotificationsOverlayInitialized() {
        return this.notificationsOverlayInitialized;
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
