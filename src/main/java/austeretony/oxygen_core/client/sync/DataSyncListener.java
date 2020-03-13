package austeretony.oxygen_core.client.sync;

@FunctionalInterface
public interface DataSyncListener {

    void synced(boolean updated);
}
