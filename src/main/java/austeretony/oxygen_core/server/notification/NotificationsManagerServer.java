package austeretony.oxygen_core.server.notification;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSendRequest;
import austeretony.oxygen_core.common.notification.Notification;
import austeretony.oxygen_core.common.notification.NotificationType;
import austeretony.oxygen_core.common.notification.event.NotificationEvent;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import javax.annotation.Nullable;
import java.util.*;

public class NotificationsManagerServer {

    private @Nullable NotificationProviderServer provider;
    private final List<RequestValidator> validators = new ArrayList<>();

    private final Map<UUID, RequestEntry> requests = new HashMap<>();

    public void registerProvider(NotificationProviderServer provider) {
        this.provider = provider;
    }

    public void registerValidator(RequestValidator validator) {
        validators.add(validator);
    }

    public boolean sendNotification(EntityPlayerMP targetMP, Notification notification) {
        return sendNotification(null, targetMP, notification);
    }

    public boolean sendNotification(@Nullable EntityPlayerMP senderMP, EntityPlayerMP targetMP, Notification notification) {
        if (senderMP != null && notification.getType() == NotificationType.REQUEST_STANDARD) {
            for (RequestValidator validator : validators) {
                if (!validator.canSendRequest(senderMP, targetMP, notification)) {
                    return false;
                }
            }
        }

        NotificationEvent event = new NotificationEvent.Receive(senderMP, targetMP, notification);
        if (MinecraftCommon.postEvent(event)) return false;
        Notification newNotification = event.getNotification();
        MinecraftCommon.postEvent(new NotificationEvent.Received(senderMP, targetMP, newNotification));

        if (newNotification.getType() != NotificationType.NOTIFICATION && !queueRequest(targetMP, newNotification)) {
            return false;
        }

        if (provider != null) {
            return provider.sendNotification(targetMP, newNotification);
        } else {
            if (newNotification.getType() == NotificationType.NOTIFICATION) {
                ITextComponent msg = new TextComponentTranslation(newNotification.getMessage(), newNotification.getArguments());
                msg.getStyle().setColor(TextFormatting.AQUA);
                targetMP.sendMessage(msg);
                return true;
            } else {
                OxygenMain.network().sendTo(new CPSendRequest(newNotification.getMessage(), newNotification.getArguments(),
                        newNotification.getUUID()), targetMP);
            }
        }
        return false;
    }

    private boolean queueRequest(EntityPlayerMP targetMP, Notification notification) {
        synchronized (requests) {
            UUID targetUUID = MinecraftCommon.getEntityUUID(targetMP);
            if (provider == null) {
                for (RequestEntry entry : requests.values()) {
                    if (MinecraftCommon.getEntityUUID(entry.getEntityPlayer()).equals(targetUUID)) {
                        return false;
                    }
                }
            }
            requests.put(notification.getUUID(), new RequestEntry(targetMP, notification));
            return true;
        }
    }

    public void update() {
        final Runnable task = () -> {
            synchronized (requests) {
                Iterator<RequestEntry> iterator = requests.values().iterator();
                while (iterator.hasNext()) {
                    RequestEntry entry = iterator.next();
                    if (OxygenServer.getCurrentTimeMillis() > entry.getExpireTimeMillis()) {
                        entry.request.expired(entry.getEntityPlayer());
                        iterator.remove();
                    }
                }
            }
        };
        OxygenServer.addTask(task);
    }

    public void acceptRequest(EntityPlayerMP playerMP, UUID requestUUID) {
        synchronized (requests) {
            RequestEntry entry = requests.get(requestUUID);
            if (entry != null) {
                entry.request.accepted(playerMP);
                requests.remove(requestUUID);
            }
        }
    }

    public void rejectRequest(EntityPlayerMP playerMP, UUID requestUUID) {
        synchronized (requests) {
            RequestEntry entry = requests.get(requestUUID);
            if (entry != null) {
                entry.request.rejected(playerMP);
                requests.remove(requestUUID);
            }
        }
    }

    static class RequestEntry {

        final long expireTimeMillis;
        final EntityPlayerMP playerMP;
        final Notification request;

        RequestEntry(EntityPlayerMP playerMP, Notification request) {
            expireTimeMillis = OxygenServer.getCurrentTimeMillis() + request.getExpirationTimeMillis();
            this.playerMP = playerMP;
            this.request = request;
        }

        long getExpireTimeMillis() {
            return expireTimeMillis;
        }

        EntityPlayerMP getEntityPlayer() {
            return playerMP;
        }

        Notification getRequest() {
            return request;
        }
    }
}
