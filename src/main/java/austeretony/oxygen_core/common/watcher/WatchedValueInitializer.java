package austeretony.oxygen_core.common.watcher;

import java.util.UUID;

public interface WatchedValueInitializer {

    void init(UUID playerUUID, WatchedValue value);
}
