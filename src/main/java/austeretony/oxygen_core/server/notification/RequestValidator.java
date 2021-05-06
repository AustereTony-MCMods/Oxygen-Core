package austeretony.oxygen_core.server.notification;

import austeretony.oxygen_core.common.notification.Notification;
import net.minecraft.entity.player.EntityPlayerMP;

@FunctionalInterface
public interface RequestValidator {

    boolean canSendRequest(EntityPlayerMP senderMP, EntityPlayerMP targetMP, Notification notification);
}
