package austeretony.oxygen_core.client.api.event;

import austeretony.oxygen_core.common.notification.Notification;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OxygenNotificationRecievedEvent extends Event {

    public final Notification notification;

    public OxygenNotificationRecievedEvent(Notification notification) {
        this.notification = notification;
    }
}
