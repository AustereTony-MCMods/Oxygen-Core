package austeretony.oxygen_core.client.input;

import austeretony.oxygen_core.client.gui.menu.OxygenMenuHelper;
import austeretony.oxygen_core.client.util.MinecraftClient;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class OxygenKeyHandler {

    private static final Map<Integer, KeyBindEntry> REGISTRY_MAP = new HashMap<>();

    private final BiMap<Integer, KeyBinding> keyBindingMap = HashBiMap.create();

    public static void registerKeyBind(int id, String name, String category, @Nonnull Supplier<Integer> keyCodeSupplier,
                                       @Nonnull Supplier<Boolean> validationSupplier, boolean checkMenuEnabled,
                                       @Nonnull Runnable task) {
        REGISTRY_MAP.put(id, new KeyBindEntry(id, name, category, keyCodeSupplier, validationSupplier, checkMenuEnabled, task));
    }

    public static Map<Integer, KeyBindEntry> getRegistry() {
        return REGISTRY_MAP;
    }

    public void registerKeyBindings() {
        REGISTRY_MAP.values()
                .stream()
                .filter(e -> e.getValidationSupplier().get() && (!e.checkMenuEnabled() || !OxygenMenuHelper.isMenuEnabled()))
                .forEach(e -> {
                    KeyBinding keyBinding = new KeyBinding(e.getName(), e.getKeyCodeSupplier().get(), e.getCategory());
                    MinecraftClient.registerKeyBinding(keyBinding);
                    keyBindingMap.put(e.getId(), keyBinding);
                });
    }

    @SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        for (KeyBinding keyBinding : keyBindingMap.values()) {
            if (keyBinding.isPressed()) {
                int id = keyBindingMap.inverse().getOrDefault(keyBinding, -1);
                KeyBindEntry entry = REGISTRY_MAP.get(id);
                if (entry != null) {
                    entry.getTask().run();
                }
            }
        }
    }

    @Nullable
    public KeyBinding getKeyBinding(int id) {
        return keyBindingMap.get(id);
    }
}
