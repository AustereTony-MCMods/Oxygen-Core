package austeretony.oxygen_core.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.event.OxygenNotificationRecievedEvent;
import austeretony.oxygen_core.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestReply;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraftforge.common.MinecraftForge;

public class NotificationManagerClient {

    private final Map<Long, Notification> notifications = new ConcurrentHashMap<>(5);

    private long latestNotificationId;

    public void openNotificationsMenu() {
        ClientReference.displayGuiScreen(new NotificationsGUIScreen());
    }

    public Map<Long, Notification> getNotifications() {
        return this.notifications;
    }

    public Notification getNotification(long id) {
        return this.notifications.get(id);
    }

    public void addNotification(Notification notification) {
        this.notifications.put(notification.getId(), notification);
        ClientReference.getClientPlayer().playSound(OxygenSoundEffects.NOTIFICATION_RECEIVED.soundEvent, 1.0F, 1.0F);
        if (notification.getType() == EnumNotification.REQUEST) {
            if (!OxygenHelperClient.getClientSettingBoolean(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING_ID))
                this.latestNotificationId = notification.getId();
        }
        ClientReference.delegateToClientThread(()->MinecraftForge.EVENT_BUS.post(new OxygenNotificationRecievedEvent(notification)));
    }

    public boolean pendingRequestExist() {
        return this.latestNotificationId != 0L;
    }

    public long getLatestRequestId() {
        return this.latestNotificationId;
    }

    public Notification getLatestRequest() {
        return this.notifications.get(this.latestNotificationId);
    }

    public void resetLatestRequestId() {       
        this.latestNotificationId = 0L;
    }

    public void processNotifications() {
        OxygenHelperClient.addRoutineTask(()->{
            Iterator<Notification> iterator = this.notifications.values().iterator();
            Notification notification;
            while (iterator.hasNext()) {
                notification = iterator.next();
                if (notification.isExpired()) {
                    if (notification.getId() == this.latestNotificationId)
                        this.latestNotificationId = 0L;
                    iterator.remove();
                }
            }
        });
    }

    public void acceptKeyPressedSynced() {
        if (!ClientReference.hasActiveGUI() && this.pendingRequestExist()) {
            this.notifications.remove(this.latestNotificationId);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.ACCEPT, this.latestNotificationId));
            this.resetLatestRequestId();
        }
    }

    public void rejectKeyPressedSynced() {
        if (!ClientReference.hasActiveGUI() && this.pendingRequestExist()) {
            this.notifications.remove(this.latestNotificationId);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.REJECT, this.latestNotificationId));
            this.resetLatestRequestId();
        }
    }

    public void reset() {
        this.notifications.clear();
        this.latestNotificationId = 0L;
    }
}
