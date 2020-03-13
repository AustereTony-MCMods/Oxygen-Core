package austeretony.oxygen_core.client.sync;

import java.util.Set;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.sync.SynchronousEntry;

public interface DataSyncHandlerClient<T extends SynchronousEntry> {

    int getDataId();

    Class<T> getDataContainerClass();

    Set<Long> getIds();

    void clearData();

    T getEntry(long entryId);

    void addEntry(T entry);

    void save();

    @Nullable
    DataSyncListener getSyncListener();
}
