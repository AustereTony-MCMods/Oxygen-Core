package austeretony.oxygen.common.notification;

import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPRequestReply;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class NotificationClient implements IOxygenNotification, Comparable<NotificationClient> {

    private final EnumNotifications type;

    private final long id;

    private final int index, expireTime;

    private final String description;

    private final String[] args;

    private int counter;

    public NotificationClient(EnumNotifications type, int index, long id, int expireTime, String description, String... args) {
        this.type = type;
        this.index = index;
        this.id = id;
        this.expireTime = type == EnumNotifications.REQUEST ? expireTime : - 1;
        this.description = description;
        this.args = args;
        this.counter = this.getExpireTime() * 20;
    }

    @Override
    public EnumNotifications getType() {
        return this.type;
    }

    @Override
    public long getId() {
        return this.id;
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
    public int getExpireTime() {
        return this.expireTime;
    }

    @Override
    public void accepted(EntityPlayer player) {
        if (this.type == EnumNotifications.REQUEST) {
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.ACCEPT, this.id));
            if (this.id == NotificationManagerClient.instance().getLatestRequestId())
                NotificationManagerClient.instance().resetLatestRequestId();
        }
        NotificationManagerClient.instance().getNotifications().remove(this.id);
    }

    @Override
    public void rejected(EntityPlayer player) {
        if (this.type == EnumNotifications.REQUEST) {
            OxygenMain.network().sendToServer(new SPRequestReply(EnumRequestReply.REJECT, this.id));
            if (this.id == NotificationManagerClient.instance().getLatestRequestId())
                NotificationManagerClient.instance().resetLatestRequestId();
        }
        NotificationManagerClient.instance().getNotifications().remove(this.id);
    }

    @Override
    public void expired() {}

    @Override
    public boolean isExpired() {
        if (this.counter > 0)
            this.counter--;
        if (this.counter == 0) 
            return true;
        return false;
    }

    @Override
    public int getCounter() {
        return this.counter;
    }

    @Override
    public int compareTo(NotificationClient other) {
        return (int) (this.id - other.id);
    }
}
