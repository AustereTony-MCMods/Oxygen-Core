package austeretony.oxygen.client.event;

import austeretony.oxygen.common.notification.IOxygenNotification;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenNotificationRecievedEvent extends Event {

    public final IOxygenNotification notification;

    public OxygenNotificationRecievedEvent(IOxygenNotification notification) {
        this.notification = notification;
    }
}
