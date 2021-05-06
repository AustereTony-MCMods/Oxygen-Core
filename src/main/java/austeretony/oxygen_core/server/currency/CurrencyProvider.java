package austeretony.oxygen_core.server.currency;

import javax.annotation.Nullable;
import java.util.UUID;

public interface CurrencyProvider {

    String getName();

    int getIndex();

    CurrencySource getSource();

    boolean isForcedSync();

    @Nullable
    Long getBalance(UUID playerUUID, String username);

    @Nullable
    Long setBalance(UUID playerUUID, String username, long value);

    @Nullable
    Long incrementBalance(UUID playerUUID, String username, long increment);

    @Nullable
    Long decrementBalance(UUID playerUUID, String username, long decrement);

    void updated(UUID playerUUID, String username, @Nullable Long balance);
}
