package austeretony.oxygen_core.client.sync;

import java.util.Set;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.sync.DataFragment;
import austeretony.oxygen_core.common.sync.SynchronizedData;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.util.internal.ConcurrentSet;

public class DataSyncProcess {

    private final Set<DataFragment> fragments = new ConcurrentSet<>();

    private final int dataId;

    private int fragmentsAmount;

    public DataSyncProcess(int dataId) {
        this.dataId = dataId;
    }

    public void add(int fragments, int entriesAmount, byte[] rawData) {
        this.fragmentsAmount = fragments;
        this.fragments.add(new DataFragment(entriesAmount, rawData));
        if (this.fragments.size() == fragments)
            this.process();
    }

    private void process() {          
        DataSyncHandlerClient<SynchronizedData> handler = OxygenManagerClient.instance().getDataSyncManager().getHandler(this.dataId);
        SynchronizedData entry;
        int i;
        ByteBuf buffer = null;
        try {
            buffer = Unpooled.buffer(Short.MAX_VALUE);
            for (DataFragment fragment : this.fragments) {
                buffer.writeBytes(fragment.rawData);
                for (i = 0; i < fragment.entriesAmount; i++) {
                    try {
                        entry = handler.getDataContainerClass().newInstance();
                        entry.read(buffer);
                        handler.addEntry(entry);
                    } catch (InstantiationException | IllegalAccessException exception) {
                        exception.printStackTrace();
                    }
                }
                buffer.clear();
            }            
            if (handler.getSyncListener() != null) {
                handler.save();
                handler.getSyncListener().synced(true);  
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }
}