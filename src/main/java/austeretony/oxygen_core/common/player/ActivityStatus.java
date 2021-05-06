package austeretony.oxygen_core.common.player;

import austeretony.oxygen_core.client.util.MinecraftClient;

public enum ActivityStatus {

    ONLINE("online"),
    AWAY("away"),
    NOT_DISTURB("not_disturb"),
    OFFLINE("offline");

    private final String name;

    ActivityStatus(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return MinecraftClient.localize("oxygen_core.activity_status." + name);
    }
}
