package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.WatcherManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.watcher.Watcher;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPSyncWatchedValues extends ProxyPacket {

    private Watcher watcher;

    private boolean forced;

    private int[] valuesIds;

    public CPSyncWatchedValues() {}

    public CPSyncWatchedValues(Watcher statWatcher, boolean forced, int... valuesIds) {
        this.watcher = statWatcher;
        this.forced = forced;
        this.valuesIds = valuesIds;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        if (this.forced)
            this.watcher.forcedSync(buffer, this.valuesIds);
        else
            this.watcher.write(buffer);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        WatcherManagerClient.instance().read(buffer);
    }
}
