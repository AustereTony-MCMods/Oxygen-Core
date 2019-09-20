package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.client.api.ClientReference;

public enum EnumOxygenStatusMessage {

    REQUEST_SENT("requestSent"),
    REQUEST_RESET("requestReset");

    private final String status;

    EnumOxygenStatusMessage(String status) {
        this.status = "oxygen.status." + status;
    }

    public String localizedName() {
        return ClientReference.localize(this.status);
    }
}
