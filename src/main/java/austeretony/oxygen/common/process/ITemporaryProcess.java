package austeretony.oxygen.common.process;

public interface ITemporaryProcess {

    long getId();

    int getExpireTime();
    
    void process();

    void expired();

    boolean isExpired();

    int getCounter();
}
