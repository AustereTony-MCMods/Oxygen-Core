package austeretony.oxygen.common.watcher;

import java.util.UUID;

public interface IValueInitializer {

    void init(UUID playerUUID, WatchedValue value);
}
