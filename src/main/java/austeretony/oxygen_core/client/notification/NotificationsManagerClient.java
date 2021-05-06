package austeretony.oxygen_core.client.notification;

import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.server.SPRequestAction;
import austeretony.oxygen_core.common.sound.SoundEffects;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class NotificationsManagerClient {

    private final Deque<UUID> requestsQueue = new ArrayDeque<>();

    private final Map<Integer, ResourceLocation> iconsMap = new HashMap<>();
    private final Map<Integer, Runnable> actionsMap = new HashMap<>();

    private NotificationProviderClient notificationProvider;

    public NotificationsManagerClient() {
        notificationProvider = new NotificationProviderClient() {

            @Override
            public void acceptLatestRequest() {
                UUID requestUUID = requestsQueue.poll();
                if (requestUUID != null) {
                    OxygenMain.network().sendToServer(new SPRequestAction(requestUUID, true));
                }
            }

            @Override
            public void rejectLatestRequest() {
                UUID requestUUID = requestsQueue.poll();
                if (requestUUID != null) {
                    OxygenMain.network().sendToServer(new SPRequestAction(requestUUID, false));
                }
            }
        };
    }

    public void registerNotificationIcon(int id, ResourceLocation icon) {
        iconsMap.put(id, icon);
    }

    public void registerNotificationAction(int id, Runnable action) {
        actionsMap.put(id, action);
    }

    public Map<Integer, ResourceLocation> getIconsMap() {
        return iconsMap;
    }

    public Map<Integer, Runnable> getActionsMap() {
        return actionsMap;
    }

    public void registerNotificationProvider(NotificationProviderClient provider) {
        notificationProvider = provider;
    }

    public NotificationProviderClient getNotificationProvider() {
        return notificationProvider;
    }

    public void addRequest(String message, String[] args, UUID requestUUID) {
        MinecraftClient.playUISound(SoundEffects.uiNotificationReceived);
        requestsQueue.push(requestUUID);

        ITextComponent msgMain = new TextComponentTranslation(message, args);
        ITextComponent msgAdditional = new TextComponentTranslation("oxygen_core.message.request.action");
        msgMain.getStyle().setColor(TextFormatting.AQUA);
        msgAdditional.getStyle().setColor(TextFormatting.GRAY);
        msgAdditional.getStyle().setItalic(true);

        MinecraftClient.showChatMessage(msgMain);
        MinecraftClient.showChatMessage(msgAdditional);
    }
}
