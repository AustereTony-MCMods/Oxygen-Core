package austeretony.oxygen_core.common.persistent;

public abstract class AbstractPersistentData implements PersistentData {

    protected volatile boolean changed;

    @Override
    public boolean isChanged() {
        return changed;
    }

    @Override
    public void markChanged() {
        changed = true;
    }

    @Override
    public void resetChangedMark() {
        changed = false;
    }
}
