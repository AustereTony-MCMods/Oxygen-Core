package austeretony.oxygen_core.common.currency;

import java.util.UUID;

public interface CurrencyProvider {
    
    String getName();

    long getCurrency(UUID playerUUID);

    boolean enoughCurrency(UUID playerUUID, long required);

    void setCurrency(UUID playerUUID, long value);

    void addCurrency(UUID playerUUID, long value);

    void removeCurrency(UUID playerUUID, long value);

    void save(UUID playerUUID);
}
