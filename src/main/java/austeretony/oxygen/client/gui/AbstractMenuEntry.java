package austeretony.oxygen.client.gui;

public abstract class AbstractMenuEntry implements IOxygenMenuEntry {

    private static int count;

    public final int index;

    public AbstractMenuEntry() {
        this.index = count++;
    }
}