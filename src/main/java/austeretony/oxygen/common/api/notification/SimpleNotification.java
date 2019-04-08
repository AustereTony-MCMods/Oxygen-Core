package austeretony.oxygen.common.api.notification;

import austeretony.oxygen.common.notification.EnumNotifications;
import net.minecraft.entity.player.EntityPlayer;

public class SimpleNotification extends AbstractNotification {

    public final String description;

    public final String[] args;

    public final int index;

    public SimpleNotification(int index, String description, String... args) {
        this.index = index;
        this.description = description;
        this.args = args;
    }

    @Override
    public EnumNotifications getType() {
        return EnumNotifications.NOTICE;
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
        return - 1;
    }

    @Override
    public void accepted(EntityPlayer player) {}

    @Override
    public void rejected(EntityPlayer player) {}

    @Override
    public void expired() {}

    @Override
    public int getCounter() {
        return 0;
    }
}
