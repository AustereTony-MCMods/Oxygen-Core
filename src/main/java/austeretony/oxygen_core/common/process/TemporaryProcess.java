package austeretony.oxygen_core.common.process;

public interface TemporaryProcess {

    long getId();

    int getExpireTimeSeconds();
    
    long getExpirationTimeStamp();

    void process();

    void expired();

    boolean isExpired();
}
