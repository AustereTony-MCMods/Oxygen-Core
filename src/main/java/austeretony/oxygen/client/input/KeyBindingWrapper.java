package austeretony.oxygen.client.input;

import austeretony.oxygen.client.core.api.ClientReference;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingWrapper {

    private boolean registered;
    
    private KeyBinding keyBinding;
        
    public void register(String name, int keyCode, String category) {
        this.keyBinding = new KeyBinding(name, keyCode, category);
        ClientReference.registerKeyBinding(this.keyBinding);
        this.registered = true;
    }
    
    public boolean registered() {
        return this.registered;
    }
    
    public KeyBinding getKeyBinding() {
        return this.keyBinding;
    }
}
