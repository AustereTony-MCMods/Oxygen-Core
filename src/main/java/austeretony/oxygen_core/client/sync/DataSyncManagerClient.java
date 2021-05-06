package austeretony.oxygen_core.client.sync;

import java.util.HashMap;
import java.util.Map;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.server.SPAbsentDataIds;
import austeretony.oxygen_core.common.network.packets.server.SPRequestDataSync;
import austeretony.oxygen_core.common.sync.SynchronousEntry;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import javax.annotation.Nullable;

public final class DataSyncManagerClient {

    private final Map<Integer, DataSyncHandlerClient> handlersMap = new HashMap<>(5);

    public void registerHandler(DataSyncHandlerClient handler) {
        handlersMap.put(handler.getDataId(), handler);
    }

    @Nullable
    public DataSyncHandlerClient getHandler(int dataId) {
        return handlersMap.get(dataId);
    }

    public void requestDataSync(int dataId) {
        if (handlersMap.containsKey(dataId)) {
            OxygenMain.network().sendToServer(new SPRequestDataSync(dataId));
        }
    }

    public void dataSyncFailed(int dataId) {
        DataSyncHandlerClient<SynchronousEntry> handler = getHandler(dataId);
        if (handler == null) return;

        DataSyncListener listener = handler.getSyncListener();
        if (listener != null) {
            listener.synced(false);
        }
    }

    public void validIdentifiersReceived(int dataId, long[] ids) {
        DataSyncHandlerClient<SynchronousEntry> handler = getHandler(dataId);
        if (handler == null) return;

        int i, j = 0;
        long[] needSync = new long[ids.length];
        Map<Long, SynchronousEntry> dataMap = handler.getDataMap();

        SynchronousEntry[] validEntries = new SynchronousEntry[ids.length];
        i = 0;
        for (long entryId : ids) {
            if (!dataMap.containsKey(entryId)) {
                if (i < 4095) {
                    needSync[i++] = entryId;
                }
            } else {
                validEntries[j++] = dataMap.get(entryId);
            }
        }

        handler.clear();
        for (SynchronousEntry validEntry : validEntries) {
            if (validEntry == null) break;
            dataMap.put(validEntry.getId(), validEntry);
        }        
        if (i == 0) {
            DataSyncListener listener = handler.getSyncListener();
            if (listener != null) {
                listener.synced(false);
            }
        } else {
            OxygenMain.network().sendToServer(new SPAbsentDataIds(dataId, needSync, i));
        }
    }

    public void rawDataReceived(int dataId, int entriesAmount, byte[] rawEntries) {
        DataSyncHandlerClient<SynchronousEntry> handler = getHandler(dataId);
        if (handler == null) return;
        Map<Long, SynchronousEntry> dataMap = handler.getDataMap();

        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer(rawEntries.length);
            buffer.writeBytes(rawEntries);

            SynchronousEntry entry;
            for (int i = 0; i < entriesAmount; i++) {
                try {
                    entry = handler.getSynchronousEntryClass().newInstance();
                    entry.read(buffer);
                    dataMap.put(entry.getId(), entry);
                } catch (InstantiationException | IllegalAccessException exception) {
                    exception.printStackTrace();
                }
            }

            handler.save();
            DataSyncListener listener = handler.getSyncListener();
            if (listener != null) {
                handler.getSyncListener().synced(true);
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
