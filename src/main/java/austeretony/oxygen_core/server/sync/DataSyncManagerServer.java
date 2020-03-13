package austeretony.oxygen_core.server.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.client.CPSyncAbsentData;
import austeretony.oxygen_core.common.network.client.CPSyncValidDataIds;
import austeretony.oxygen_core.common.sync.SynchronousEntry;
import austeretony.oxygen_core.server.network.NetworkRequestsRegistryServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

public class DataSyncManagerServer {

    private final Map<Integer, DataSyncHandlerServer> handlers = new HashMap<>(5);

    public void registerHandler(DataSyncHandlerServer handler) {
        this.handlers.put(handler.getDataId(), handler);

        NetworkRequestsRegistryServer.registerRequest(handler.getDataId() + 1000, 2000);
        NetworkRequestsRegistryServer.registerRequest(handler.getDataId() + 2000, 2000);
    }

    public void syncData(EntityPlayerMP playerMP, int dataId) {
        DataSyncHandlerServer<SynchronousEntry> handler = this.getHandler(dataId);
        if (handler != null) {         
            UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
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
        DataSyncHandlerServer<SynchronousEntry> handler = this.getHandler(dataId);
        if (handler != null) {
            UUID playerUUID = CommonReference.getPersistentUUID(playerMP);
            if (!handler.allowSync(playerUUID))
                return;
            if (ids.length <= handler.getIds(playerUUID).size()) {
                ByteBuf buffer = null;
                try {
                    buffer = Unpooled.buffer(Short.MAX_VALUE / 4);

                    int entriesAmount = 0;
                    SynchronousEntry entry;
                    for (long id : ids) {
                        entry = handler.getEntry(playerUUID, id);
                        if (entry != null) {
                            entry.write(buffer);
                            entriesAmount++;
                        }
                    }

                    //Note 0.10: Removed data sync with fragmentation.
                    //
                    //diesieben07 from www.minecraftforge.net forum said server -> client packet 
                    //max payload is 200MB, so Oxygen will sync specified data with single packet.

                    if (entriesAmount > 0) {
                        if (buffer.writerIndex() > 209715200) {
                            OxygenMain.LOGGER.error("[Core] Data {} synchronization buffer exceeds maximum packet payload ({}/209715200 bytes) for player {}, it will not be synchronized!",
                                    dataId,
                                    buffer.writerIndex(),
                                    CommonReference.getName(playerMP));
                        } else {
                            byte[] rawEntries = new byte[buffer.writerIndex()];
                            buffer.readBytes(rawEntries);
                            OxygenMain.network().sendTo(new CPSyncAbsentData(dataId, entriesAmount, rawEntries), playerMP);
                        }
                    }
                } finally {
                    if (buffer != null)
                        buffer.release();
                }
            }
        }
    }
}
