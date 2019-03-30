package austeretony.oxygen.common.util;

public class TimeCounter {

    private long current, last, delay;

    private volatile boolean expired;

    public TimeCounter(long actionDelay) {
        this.delay = actionDelay;
        this.last = System.currentTimeMillis();
    }

    public boolean expired() {
        this.current = System.currentTimeMillis();
        if (this.expired || (this.current - this.last > this.delay)) {
            this.expired = false;
            this.last = this.current;
            return true;
        }
        return false;
    }

    public void setExpired() {
        this.expired = true;
    }
}
