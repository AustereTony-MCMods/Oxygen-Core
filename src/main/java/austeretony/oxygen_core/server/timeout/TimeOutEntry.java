package austeretony.oxygen_core.server.timeout;

public class TimeOutEntry {

    private final int timeOutMillis;

    private volatile long timeOutTimeStampMillis;

    public TimeOutEntry(int timeOutMillis) {
        this.timeOutMillis = timeOutMillis;
    }

    public boolean checkTimeOut() {
        return System.currentTimeMillis() < this.timeOutTimeStampMillis;
    }

    public void resetTimeOut() {
        this.timeOutTimeStampMillis = System.currentTimeMillis() + this.timeOutMillis;
    }
}
