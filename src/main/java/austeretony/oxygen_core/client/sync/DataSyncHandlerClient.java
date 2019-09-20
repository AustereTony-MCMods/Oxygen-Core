package austeretony.oxygen_core.client.sync;

import java.util.Set;

import austeretony.oxygen_core.common.sync.SynchronizedData;

public interface DataSyncHandlerClient<T extends SynchronizedData> {

    int getDataId();

    Class<T> getDataContainerClass();

    Set<Long> getIds();

    void clearData();

    T getEntry(long entryId);

    void addEntry(T entry);

    void save();

    DataSyncListener getSyncListener();
}