package austeretony.oxygen.client;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.client.api.event.OxygenNotificationRecievedEvent;
import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.client.gui.notifications.NotificationsGUIScreen;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenSoundEffects;
import austeretony.oxygen.common.network.server.SPRequestReply;
import austeretony.oxygen.common.notification.EnumNotification;
import austeretony.oxygen.common.notification.EnumRequestReply;
import austeretony.oxygen.common.notification.INotification;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

public class NotificationManagerClient {

    private final Map<Long, INotification> notifications = new ConcurrentHashMap<Long, INotification>(5);

    private final Map<Integer, ResourceLocation> icons = new HashMap<Integer, ResourceLocation>(5);

    private long latestNotificationId;

    private boolean notificationsExist;

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

    public Map<Long, INotification> getNotifications() {
        return this.notifications;
    }

    public INotification getNotification(long id) {
        return this.notifications.get(id);
    }

    public void addNotification(INotification notification) {
        this.notifications.put(notification.getId(), notification);
        if (notification.getType() == EnumNotification.REQUEST) {
            ClientReference.getClientPlayer().playSound(OxygenSoundEffects.REQUEST_RECEIVED.soundEvent, 1.0F, 1.0F);//request received sound effect
            if (!OxygenHelperClient.getClientSettingBoolean(OxygenMain.HIDE_REQUESTS_OVERLAY_SETTING_ID))
                this.latestNotificationId = notification.getId();
        }
        this.notificationsExist = true;
        MinecraftForge.EVENT_BUS.post(new OxygenNotificationRecievedEvent(notification));//custom forge event   
    }

    public boolean latestRequestIdExist() {
        return this.latestNotificationId != 0L;
    }

    public long getLatestRequestId() {
        return this.latestNotificationId;
    }

    public INotification getLatestRequest() {
        return this.notifications.get(this.latestNotificationId);
    }

    public void resetLatestRequestId() {       
        this.latestNotificationId = 0L;
    }

    public void processNotifications() {
        if (this.notificationsExist) {
            Iterator<INotification> iterator = this.notifications.values().iterator();
            INotification notification;
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
