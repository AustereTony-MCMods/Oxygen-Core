package austeretony.oxygen.common.api.notification;

import austeretony.oxygen.common.notification.INotification;

public abstract class AbstractNotification implements INotification, Comparable<AbstractNotification> {

    private final long id;

    protected int counter;

    public AbstractNotification() {
        this.counter = this.getExpireTime() * 20;
        this.id = System.nanoTime();
    }

    @Override
    public long getId() {
        return this.id;
    }

    @Override
    public boolean isExpired() {
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
    public int compareTo(AbstractNotification other) {        
        return (int) (this.id - other.id);
    }
}
