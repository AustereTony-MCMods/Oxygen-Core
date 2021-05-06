package austeretony.oxygen_core.client.settings.gui.alignment;

import austeretony.oxygen_core.client.util.MinecraftClient;

public enum ScreenAlignment {

    LEFT("left"),
    CENTER("center"),
    RIGHT("right");

    private final String name;

    ScreenAlignment(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return MinecraftClient.localize("oxygen_core.gui.alignment." + name);
    }
}
