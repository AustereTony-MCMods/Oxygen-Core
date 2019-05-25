package austeretony.oxygen.client.api.event;

import austeretony.oxygen.common.notification.INotification;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenNotificationRecievedEvent extends Event {

    public final INotification notification;

    public OxygenNotificationRecievedEvent(INotification notification) {
        this.notification = notification;
    }
}
