package austeretony.oxygen_core.client.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPAbsentDataIds;
import austeretony.oxygen_core.common.network.server.SPStartDataSync;
import austeretony.oxygen_core.common.sync.SynchronizedData;

public class DataSyncManagerClient {

    private final Map<Integer, DataSyncHandlerClient> handlers = new HashMap<>(5);

    private final Map<Integer, DataSyncProcess> synchronizations = new ConcurrentHashMap<>(5);

    public void registerHandler(DataSyncHandlerClient handler) {
        this.handlers.put(handler.getDataId(), handler);
    }

    public void syncData(int dataId) {
        if (this.handlers.containsKey(dataId))
            OxygenMain.network().sendToServer(new SPStartDataSync(dataId));
    }

    public DataSyncHandlerClient getHandler(int dataId) {
        return this.handlers.get(dataId);
    }

    public void validIdentifiersReceived(int dataId, long[] ids) {
        DataSyncHandlerClient handler = this.getHandler(dataId);
        int 
        i = 0, 
        j = 0;
        long[] needSync = new long[ids.length];
        Set<Long> clientIds = handler.getIds();
        SynchronizedData[] validEntries = new SynchronizedData[ids.length];
        i = 0;
        for (long entryId : ids)
            if (!clientIds.contains(entryId))
                needSync[i++] = entryId;    
            else
                validEntries[j++] = handler.getEntry(entryId);
        handler.clearData();
        for (SynchronizedData validEntry : validEntries) {
            if (validEntry == null) break;
            handler.addEntry(validEntry);
        }        
        if (i == 0) {
            if (handler.getSyncListener() != null)
                handler.getSyncListener().synced(false);  
        } else 
            this.synchronizations.put(dataId, new DataSyncProcess(dataId));
        OxygenMain.network().sendToServer(new SPAbsentDataIds(dataId, needSync, i));
    }

    public void rawDataReceived(int dataId, int fragments, int entriesAmount, byte[] rawData) {
        this.synchronizations.get(dataId).add(fragments, entriesAmount, rawData);
    }
}