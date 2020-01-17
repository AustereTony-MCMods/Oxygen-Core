package austeretony.oxygen_core.common.api.process;

import austeretony.oxygen_core.common.process.TemporaryProcess;

public abstract class AbstractTemporaryProcess implements TemporaryProcess, Comparable<TemporaryProcess> {

    private final long id, expireTimeMillis;

    public AbstractTemporaryProcess() {
        this.id = System.nanoTime();
        this.expireTimeMillis = System.currentTimeMillis() + this.getExpireTimeSeconds() * 1000L;
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public long getExpirationTimeStamp() {
        return this.expireTimeMillis;
    }

    @Override
    public boolean isExpired() {
        this.process();
        if (System.currentTimeMillis() >= this.expireTimeMillis) {
            this.expired();
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(TemporaryProcess other) {        
        return other.getId() < this.getId() ? - 1 : other.getId() > this.getId() ? 1 : 0;
    }
}
