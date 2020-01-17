package austeretony.oxygen_core.common.persistent;

public abstract class AbstractPersistentData implements PersistentData {

    private volatile boolean changed;

    @Override
    public boolean isChanged() {
        return this.changed;
    }

    @Override
    public void setChanged(boolean flag) { 
        this.changed = flag;
    }
}
