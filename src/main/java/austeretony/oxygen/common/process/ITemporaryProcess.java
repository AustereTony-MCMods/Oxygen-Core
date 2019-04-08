package austeretony.oxygen.common.process;

public interface ITemporaryProcess {

    long getId();

    int getExpireTime();

    void expired();

    boolean isExpired();
    
    int getCounter();
}
