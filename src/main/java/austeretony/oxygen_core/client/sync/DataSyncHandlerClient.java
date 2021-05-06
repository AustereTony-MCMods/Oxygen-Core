package austeretony.oxygen_core.client.sync;

import java.util.Map;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.sync.SynchronousEntry;

public interface DataSyncHandlerClient<T extends SynchronousEntry> {

    int getDataId();

    Class<T> getSynchronousEntryClass();

    Map<Long, T> getDataMap();

    void clear();

    void save();

    @Nullable
    DataSyncListener getSyncListener();
}
