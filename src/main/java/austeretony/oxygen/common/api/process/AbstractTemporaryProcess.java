package austeretony.oxygen.common.api.process;

import austeretony.oxygen.common.process.ITemporaryProcess;

public abstract class AbstractTemporaryProcess implements ITemporaryProcess, Comparable<AbstractTemporaryProcess> {

    private final long id;

    protected int counter;

    public AbstractTemporaryProcess() {
        this.counter = this.getExpireTime() * 20;
        this.id = System.nanoTime();
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isExpired() {
        this.process();
        if (this.counter > 0)
            this.counter--;
        if (this.counter == 0) {
            this.expired();
            return true;
        }
        return false;
    }

    @Override
    public int getCounter() {
        return this.counter;
    }

    @Override
    public int compareTo(AbstractTemporaryProcess other) {        
        return (int) (this.id - other.id);
    }
}
