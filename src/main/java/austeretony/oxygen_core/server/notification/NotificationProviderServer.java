package austeretony.oxygen_core.server.notification;

import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.entity.player.EntityPlayerMP;

public interface NotificationProviderServer {

    boolean sendNotification(EntityPlayerMP targetMP, Notification notification);
}
