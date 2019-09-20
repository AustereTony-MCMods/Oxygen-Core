package austeretony.oxygen_core.client.gui.menu;

public interface OxygenMenuEntry {

    String getName();
    
    int getIndex();

    boolean isValid();

    void open();
}