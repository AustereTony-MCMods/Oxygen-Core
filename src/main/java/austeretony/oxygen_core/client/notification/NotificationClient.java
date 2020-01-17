package austeretony.oxygen_core.client.notification;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPRequestReply;
import austeretony.oxygen_core.common.notification.EnumNotification;
import austeretony.oxygen_core.common.notification.EnumRequestReply;
import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.entity.player.EntityPlayer;

public class NotificationClient implements Notification, Comparable<NotificationClient> {

    private final EnumNotification type;

    private final long id, expireTime;

    private final int index;

    private final String description;

    private final String[] args;

    public NotificationClient(EnumNotification type, int index, long id, int expireTimeSeconds, String description, String... args) {
        this.type = type;
        this.index = index;
        this.id = id;
        this.description = description;
        this.args = args;
        this.expireTime = System.currentTimeMillis() + expireTimeSeconds * 1000;
    }

    @Override
    public EnumNotification getType() {
        return this.type;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public long getExpirationTimeStamp() {
        return this.expireTime;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String[] getArguments() {
        return this.args;
    }

    @Override
    public int getIndex() {
        return this.index;
    }

    @Override
    public int getExpireTimeSeconds() {
        return - 1;
    }

    @Override
    public void accepted(EntityPlayer player) {
        if (this.type == EnumNotification.REQUEST) {
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.ACCEPT, this.id));
            if (this.id == OxygenManagerClient.instance().getNotificationsManager().getLatestRequestId())
                OxygenManagerClient.instance().getNotificationsManager().resetLatestRequestId();
        }
        OxygenManagerClient.instance().getNotificationsManager().getNotifications().remove(this.id);
    }

    @Override
    public void rejected(EntityPlayer player) {
        if (this.type == EnumNotification.REQUEST) {
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.REJECT, this.id));
            if (this.id == OxygenManagerClient.instance().getNotificationsManager().getLatestRequestId())
                OxygenManagerClient.instance().getNotificationsManager().resetLatestRequestId();
        }
        OxygenManagerClient.instance().getNotificationsManager().getNotifications().remove(this.id);
    }

    @Override
    public void process() {}

    @Override
    public void expired() {}

    @Override
    public boolean isExpired() {
        if (this.type == EnumNotification.NOTIFICATION)
            return false;
        if (System.currentTimeMillis() >= this.expireTime)
            return true;
        return false;
    }

    @Override
    public int compareTo(NotificationClient other) {
        return other.getId() < this.getId() ? - 1 : other.getId() > this.getId() ? 1 : 0;
    }
}
