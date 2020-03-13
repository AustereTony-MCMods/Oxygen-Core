package austeretony.oxygen_core.common.main;

import austeretony.oxygen_core.client.api.ClientReference;

public enum EnumOxygenStatusMessage {

    ACTIVITY_STATUS_CHANGED("activityStatusChanged"),

    REQUEST_SENT("requestSent"),
    REQUEST_RESET("requestReset"),

    ROLE_CREATED("roleCreated"),
    ROLE_EDITED("roleEdited"),
    ROLE_REMOVED("roleRemoved"),
    PRIVILEGE_ADDED("privilegeAdded"),
    PRIVILEGE_REMOVED("privilegeRemoved"),
    DEFAULT_PRIVILEGE_ADDED("defaultPrivilegeAdded"),
    DEFAULT_PRIVILEGE_REMOVED("defaultPrivilegeRemoved"),
    ROLE_ADDED_TO_PLAYER("roleAddedToPlayer"),
    ROLE_REMOVED_FROM_PLAYER("roleRemovedFromPlayer"),

    ACTION_TIMEOUT("actionTimeOut"),
    INVENTORY_FULL("inventoryFull"),
    MAILBOX_FULL("mailboxFull");

    private final String status;

    EnumOxygenStatusMessage(String status) {
        this.status = "oxygen_core.status.message." + status;
    }

    public String localized() {
        return ClientReference.localize(this.status);
    }
}
