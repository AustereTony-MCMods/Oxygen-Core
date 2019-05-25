package austeretony.oxygen.common.watcher;

import java.util.UUID;

public interface IStatInitializer {

    void init(UUID playerUUID, WatchedValue value);
}
