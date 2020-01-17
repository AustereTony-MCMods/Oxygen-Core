package austeretony.oxygen_core.server.currency;

import java.util.UUID;

public interface CurrencyProvider {

    String getDisplayName();

    int getIndex();

    boolean forceSync();

    long getCurrency(UUID playerUUID);

    void setCurrency(UUID playerUUID, long value);

    void updated(UUID playerUUID);
}
