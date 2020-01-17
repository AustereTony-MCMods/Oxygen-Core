package austeretony.oxygen_core.common;

import austeretony.oxygen_core.client.api.ClientReference;

public enum EnumActivityStatus {

    ONLINE("online"),
    AWAY("away"),
    NOT_DISTURB("notDisturb"),
    OFFLINE("offline");

    public final String name;

    EnumActivityStatus(String name) {
        this.name = name;
    }

    public String localized() {
        return ClientReference.localize("oxygen_core.status." + this.name);
    }
}
