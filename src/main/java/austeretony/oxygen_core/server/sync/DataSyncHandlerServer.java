package austeretony.oxygen_core.server.sync;

import java.util.Map;
import java.util.UUID;

import austeretony.oxygen_core.common.sync.SynchronousEntry;

import javax.annotation.Nonnull;

public interface DataSyncHandlerServer<T extends SynchronousEntry> {

    int getDataId();

    boolean allowSync(UUID playerUUID);

    @Nonnull
    Map<Long, T> getDataMap(UUID playerUUID);
}
