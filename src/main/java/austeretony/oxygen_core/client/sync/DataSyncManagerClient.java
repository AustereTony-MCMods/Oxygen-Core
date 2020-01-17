package austeretony.oxygen_core.client.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.server.SPAbsentDataIds;
import austeretony.oxygen_core.common.network.server.SPStartDataSync;
import austeretony.oxygen_core.common.sync.SynchronousEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class DataSyncManagerClient {

    private final Map<Integer, DataSyncHandlerClient> handlers = new HashMap<>(5);

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
        DataSyncHandlerClient<SynchronousEntry> handler = this.getHandler(dataId);
        int 
        i = 0, 
        j = 0;
        long[] needSync = new long[ids.length];
        Set<Long> clientIds = handler.getIds();
        SynchronousEntry[] validEntries = new SynchronousEntry[ids.length];
        i = 0;
        for (long entryId : ids)
            if (!clientIds.contains(entryId))
                needSync[i++] = entryId;    
            else
                validEntries[j++] = handler.getEntry(entryId);
        handler.clearData();
        for (SynchronousEntry validEntry : validEntries) {
            if (validEntry == null) break;
            handler.addEntry(validEntry);
        }        
        if (i == 0) {
            if (handler.getSyncListener() != null)
                handler.getSyncListener().synced(false);  
        }
        OxygenMain.network().sendToServer(new SPAbsentDataIds(dataId, needSync, i));
    }

    public void rawDataReceived(int dataId, int entriesAmount, byte[] rawEntries) {
        DataSyncHandlerClient<SynchronousEntry> handler = this.getHandler(dataId);
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer(Short.MAX_VALUE / 4);
            buffer.writeBytes(rawEntries);

            SynchronousEntry entry;
            for (int i = 0; i < entriesAmount; i++) {
                try {
                    entry = handler.getDataContainerClass().newInstance();
                    entry.read(buffer);
                    handler.addEntry(entry);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }

            handler.save();
            if (handler.getSyncListener() != null)
                handler.getSyncListener().synced(true);  
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }
}
