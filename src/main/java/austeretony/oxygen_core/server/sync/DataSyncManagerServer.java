package austeretony.oxygen_core.server.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.oxygen_core.common.network.packets.client.CPDataSyncFailed;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPSyncAbsentData;
import austeretony.oxygen_core.common.network.packets.client.CPSyncValidDataIds;
import austeretony.oxygen_core.common.sync.SynchronousEntry;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;

public final class DataSyncManagerServer {

    private final Map<Integer, DataSyncHandlerServer> handlersMap = new HashMap<>(5);

    public void registerHandler(DataSyncHandlerServer handler) {
        handlersMap.put(handler.getDataId(), handler);
        OxygenServer.registerNetworkRequest(handler.getDataId() + 10500, 1000);
        OxygenServer.registerNetworkRequest(handler.getDataId() + 11500, 1000);
    }

    public void syncData(EntityPlayerMP playerMP, int dataId) {
        DataSyncHandlerServer<SynchronousEntry> handler = getHandler(dataId);
        if (handler == null) return;

        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        if (!handler.allowSync(playerUUID)) {
            OxygenMain.network().sendTo(new CPDataSyncFailed(dataId), playerMP);
            return;
        }

        Map<Long, SynchronousEntry> dataMap = handler.getDataMap(playerUUID);
        long[] ids = new long[dataMap.size()];
        int index = 0;
        for (long id : dataMap.keySet()) {
            ids[index++] = id;
        }
        OxygenMain.network().sendTo(new CPSyncValidDataIds(dataId, ids), playerMP);
    }

    @Nullable
    public DataSyncHandlerServer getHandler(int dataId) {
        return handlersMap.get(dataId);
    }

    public void syncAbsentData(EntityPlayerMP playerMP, int dataId, long[] ids) {
        DataSyncHandlerServer<SynchronousEntry> handler = getHandler(dataId);
        if (handler == null) return;

        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        if (!handler.allowSync(playerUUID)) {
            OxygenMain.network().sendTo(new CPDataSyncFailed(dataId), playerMP);
            return;
        }

        Map<Long, SynchronousEntry> dataMap = handler.getDataMap(playerUUID);
        if (ids.length > dataMap.size()) return;

        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer();

            int entriesAmount = 0;
            SynchronousEntry entry;
            for (long id : ids) {
                entry = dataMap.get(id);
                if (entry != null) {
                    entry.write(buffer);
                    entriesAmount++;
                }
            }

            if (entriesAmount > 0) {
                if (buffer.writerIndex() > 200 * 1024 * 1024) {
                    OxygenMain.logError(1, "[Core] Data {} synchronization buffer exceeds maximum packet " +
                                    "payload ({}/209715200 bytes) for player {}, it will not be synchronized!",
                            dataId, buffer.writerIndex(), MinecraftCommon.getEntityName(playerMP));
                } else {
                    byte[] rawEntries = new byte[buffer.writerIndex()];
                    buffer.readBytes(rawEntries);
                    OxygenMain.network().sendTo(new CPSyncAbsentData(dataId, entriesAmount, rawEntries), playerMP);
                }
            }
        } finally {
            if (buffer != null) {
                buffer.release();
            }
        }
    }
}
