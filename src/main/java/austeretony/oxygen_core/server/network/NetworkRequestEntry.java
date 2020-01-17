package austeretony.oxygen_core.server.network;

public class NetworkRequestEntry {

    private final int cooldownMillis;

    private volatile long cooldownExpireTimeStampMillis;

    public NetworkRequestEntry(int cooldownMillis) {
        this.cooldownMillis = cooldownMillis;
    }

    public boolean requestAvailable() {
        boolean ready = System.currentTimeMillis() > this.cooldownExpireTimeStampMillis;
        if (ready) 
            this.cooldownExpireTimeStampMillis = System.currentTimeMillis() + this.cooldownMillis;
        return ready;
    }
}
