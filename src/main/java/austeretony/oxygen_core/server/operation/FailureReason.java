package austeretony.oxygen_core.server.operation;

public enum FailureReason {

    NO_FAILURE("no_failure"),
    GENERIC("generic"),
    VALIDATION_TIMEOUT("validation_timeout"),
    INSUFFICIENT_FUNDS("insufficient_funds"),
    NOT_ENOUGH_ITEMS("not_enough_items"),
    NOT_ENOUGH_FREE_INVENTORY_SPACE("not_enough_free_inventory_space");

    private final String name;

    FailureReason(String name) {
        this.name = name;
    }

    public String getStatusMessageKey() {
        return "oxygen_core.operation.failure_reason." + name;
    }
}
