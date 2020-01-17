package austeretony.oxygen_core.server.sync;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.common.sync.SynchronousEntry;

public interface DataSyncHandlerServer<T extends SynchronousEntry> {

    int getDataId();

    boolean allowSync(UUID playerUUID);

    Set<Long> getIds(UUID playerUUID);

    T getEntry(UUID playerUUID, long entryId);
}
