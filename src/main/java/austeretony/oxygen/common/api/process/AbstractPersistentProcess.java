package austeretony.oxygen.common.api.process;

import austeretony.oxygen.common.process.IPersistentProcess;

public abstract class AbstractPersistentProcess implements IPersistentProcess {

    private int counter;

    public void run() {
        if (this.hasDelay()) {
            this.counter++;
            if (this.counter >= this.getExecutionDelay()) {
                this.counter = 0;
                this.execute();
            }         
        } else
            this.execute();
    }
}
