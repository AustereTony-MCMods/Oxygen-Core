package austeretony.oxygen_core.client.gui.base.listener;

@FunctionalInterface
public interface KeyPressListener {

    void press(char typedChar, int keyCode);
}
