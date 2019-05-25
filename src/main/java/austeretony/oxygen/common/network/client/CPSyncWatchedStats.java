package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenStatWatcherManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.watcher.StatWatcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncWatchedStats extends ProxyPacket {

    private StatWatcher statWatcher;

    private boolean forced;

    private int[] statIds;

    public CPSyncWatchedStats() {}

    public CPSyncWatchedStats(StatWatcher statWatcher, boolean forced, int... statIds) {
        this.statWatcher = statWatcher;
        this.forced = forced;
        this.statIds = statIds;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        if (this.forced)
            this.statWatcher.forcedSync(buffer, this.statIds);
        else
            this.statWatcher.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        OxygenStatWatcherManagerClient.instance().read(buffer);
    }
}
