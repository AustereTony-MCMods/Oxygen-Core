package austeretony.oxygen_core.client;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.EnumBaseClientSetting;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.event.OxygenNotificationRecievedEvent;
import austeretony.oxygen_core.client.gui.notifications.NotificationsScreen;
import austeretony.oxygen_core.client.settings.EnumCoreClientSetting;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestReply;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.sound.OxygenSoundEffects;
import net.minecraftforge.common.MinecraftForge;

public class NotificationManagerClient {

    private final Map<Long, Notification> notifications = new ConcurrentHashMap<>();

    private volatile long latestNotification;

    public void openNotificationsMenu() {
        ClientReference.displayGuiScreen(new NotificationsScreen());
    }

    public Map<Long, Notification> getNotifications() {
        return this.notifications;
    }

    public Notification getNotification(long id) {
        return this.notifications.get(id);
    }

    public void addNotification(Notification notification) {
        this.notifications.put(notification.getId(), notification);
        if (EnumBaseClientSetting.ENABLE_SOUND_EFFECTS.get().asBoolean())
            ClientReference.getClientPlayer().playSound(OxygenSoundEffects.NOTIFICATION_RECEIVED.soundEvent, 1.0F, 1.0F);
        if (notification.getType() == EnumNotification.REQUEST) {
            if (!EnumCoreClientSetting.HIDE_REQUESTS_OVERLAY.get().asBoolean())
                this.latestNotification = notification.getId();
        }
        ClientReference.delegateToClientThread(()->MinecraftForge.EVENT_BUS.post(new OxygenNotificationRecievedEvent(notification)));
    }

    public boolean pendingRequestExist() {
        return this.latestNotification != 0L;
    }

    public long getLatestRequestId() {
        return this.latestNotification;
    }

    public Notification getLatestRequest() {
        return this.notifications.get(this.latestNotification);
    }

    public void resetLatestRequestId() {       
        this.latestNotification = 0L;
    }

    public void process() {
        OxygenHelperClient.addRoutineTask(()->{
            Iterator<Notification> iterator = this.notifications.values().iterator();
            Notification notification;
            while (iterator.hasNext()) {
                notification = iterator.next();
                if (notification.isExpired()) {
                    if (notification.getId() == this.latestNotification)
                        this.latestNotification = 0L;
                    iterator.remove();
                }
            }
        });
    }

    public void acceptRequestSynced() {
        if (this.pendingRequestExist()) {
            this.notifications.remove(this.latestNotification);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.ACCEPT, this.latestNotification));
            this.resetLatestRequestId();
        }
    }

    public void rejectRequestSynced() {
        if (this.pendingRequestExist()) {
            this.notifications.remove(this.latestNotification);
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.REJECT, this.latestNotification));
            this.resetLatestRequestId();
        }
    }

    public void reset() {
        this.notifications.clear();
        this.latestNotification = 0L;
    }
}
