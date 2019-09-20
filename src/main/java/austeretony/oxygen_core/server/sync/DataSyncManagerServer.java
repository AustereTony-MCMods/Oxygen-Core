package austeretony.oxygen_core.server.sync;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncDataFragment;
import austeretony.oxygen_core.common.network.client.CPSyncValidDataIds;
import austeretony.oxygen_core.common.sync.DataFragment;
import austeretony.oxygen_core.common.sync.SynchronizedData;
import austeretony.oxygen_core.server.api.RequestsFilterHelper;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

public class DataSyncManagerServer {

    private final Map<Integer, DataSyncHandlerServer> handlers = new HashMap<>(5);

    public void registerHandler(DataSyncHandlerServer handler) {
        this.handlers.put(handler.getDataId(), handler);
        RequestsFilterHelper.registerNetworkRequest(handler.getDataId() + 100, 2);
        RequestsFilterHelper.registerNetworkRequest(handler.getDataId() + 200, 2);
    }

    public void syncData(EntityPlayerMP playerMP, int dataId) {
        if (this.handlers.containsKey(dataId)) {         
            UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
            DataSyncHandlerServer handler = this.getHandler(dataId);
            if (!handler.allowSync(playerUUID))
                return;
            Set<Long> idsSet = handler.getIds(playerUUID);
            long[] ids = new long[idsSet.size()];
            int index = 0;
            for (long id : idsSet)
                ids[index++] = id;
            OxygenMain.network().sendTo(new CPSyncValidDataIds(dataId, ids), playerMP);
        }
    }

    public DataSyncHandlerServer getHandler(int dataId) {
        return this.handlers.get(dataId);
    }

    public void syncAbsentData(EntityPlayerMP playerMP, int dataId, long[] ids) {
        if (this.handlers.containsKey(dataId)) {
            UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
            ByteBuf buffer = null;
            try {
                DataSyncHandlerServer handler = this.getHandler(dataId);
                buffer = Unpooled.buffer(Short.MAX_VALUE / 2);
                int 
                maxPayload = Short.MAX_VALUE - (4 * Byte.SIZE + 2 * Short.SIZE),
                entriesCount = 0,
                prevWriterIndex = 0;
                byte[] rawEntries;
                List<DataFragment> fragments = new ArrayList<>(3);

                SynchronizedData entry;
                for (long id : ids) {
                    entry = handler.getEntry(playerUUID, id);
                    if (entry != null) {
                        prevWriterIndex = buffer.writerIndex();
                        entry.write(buffer);
                        entriesCount++;
                        if (buffer.writerIndex() > maxPayload) {//if buffer overloaded
                            buffer.writerIndex(prevWriterIndex);
                            rawEntries = new byte[prevWriterIndex];
                            buffer.readBytes(rawEntries);
                            fragments.add(new DataFragment(entriesCount - 1, rawEntries));
                            buffer.clear();
                            entriesCount = 0;

                            prevWriterIndex = buffer.writerIndex();
                            entry.write(buffer);
                            entriesCount++;
                        }
                    }
                }

                //if no overload
                rawEntries = new byte[buffer.writerIndex()];
                buffer.readBytes(rawEntries);
                fragments.add(new DataFragment(entriesCount, rawEntries));
                for (DataFragment fragment : fragments)
                    OxygenMain.network().sendTo(new CPSyncDataFragment(dataId, fragments.size(), fragment.entriesAmount, fragment.rawData), playerMP);
            } finally {
                if (buffer != null)
                    buffer.release();
            }
        }
    }
}