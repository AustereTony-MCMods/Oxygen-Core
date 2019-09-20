package austeretony.oxygen_core.common.api.notification;

import austeretony.oxygen_core.common.notification.EnumNotification;
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
    public EnumNotification getType() {
        return EnumNotification.NOTIFICATION;
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
    public void process() {}

    @Override
    public void accepted(EntityPlayer player) {}

    @Override
    public void rejected(EntityPlayer player) {}

    @Override
    public void expired() {}
    
    @Override
    public boolean isExpired() {
        return false;
    }
}
