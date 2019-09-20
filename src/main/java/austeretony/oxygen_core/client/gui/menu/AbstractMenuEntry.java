package austeretony.oxygen_core.client.gui.menu;

public abstract class AbstractMenuEntry implements OxygenMenuEntry {

    private static int count;

    public final int index;

    public AbstractMenuEntry() {
        this.index = count++;
    }
	
	@Override
    public int getIndex() {
        return this.index;
    }
}