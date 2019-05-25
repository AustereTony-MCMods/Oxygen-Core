package austeretony.oxygen.common.process;

public interface IPersistentProcess {

    boolean hasDelay();

    int getExecutionDelay();

    void run();

    void execute();
}
