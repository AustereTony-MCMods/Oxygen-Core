package austeretony.oxygen_core.common.notification;

import austeretony.oxygen_core.client.util.MinecraftClient;

public enum NotificationType {

    NOTIFICATION("notification"),
    REQUEST_STANDARD("request_standard"),
    REQUEST_VOTING("request_voting");

    private final String name;

    NotificationType(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return MinecraftClient.localize("oxygen_core.notification." + name);
    }
}
