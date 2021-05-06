package austeretony.oxygen_core.common.notification.event;

import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.annotation.Nullable;

public class NotificationEvent extends Event {

    @Nullable
    private final EntityPlayer sender;
    private final EntityPlayer target;
    private Notification notification;

    public NotificationEvent(@Nullable EntityPlayer sender, EntityPlayer target, Notification notification) {
        this.sender = sender;
        this.target = target;
        this.notification = notification;
    }

    @Nullable
    public EntityPlayer getSenderPlayer() {
        return sender;
    }

    public EntityPlayer getTargetPlayer() {
        return target;
    }

    public Notification getNotification() {
        return notification;
    }

    @Cancelable
    public static class Receive extends NotificationEvent {

        public Receive(@Nullable EntityPlayer sender, EntityPlayer target, Notification notification) {
            super(sender, target, notification);
        }

        public void setNotification(Notification notification) {
            super.notification = notification;
        }
    }

    public static class Received extends NotificationEvent {

        public Received(@Nullable EntityPlayer sender, EntityPlayer target, Notification notification) {
            super(sender, target, notification);
        }
    }


}
