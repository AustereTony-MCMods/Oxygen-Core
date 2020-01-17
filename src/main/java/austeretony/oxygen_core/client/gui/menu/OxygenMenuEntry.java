package austeretony.oxygen_core.client.gui.menu;

public interface OxygenMenuEntry {

    int getId();
    
    String getLocalizedName();

    int getKeyCode();

    boolean isValid();

    void open();
}
