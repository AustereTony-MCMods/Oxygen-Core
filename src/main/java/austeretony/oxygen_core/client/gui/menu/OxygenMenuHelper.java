package austeretony.oxygen_core.client.gui.menu;

import net.minecraft.client.settings.KeyBinding;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public final class OxygenMenuHelper {

    private static final Map<Integer, OxygenMenuEntry> MENU_ENTRIES_MAP = new HashMap<>();

    private static boolean menuEnabled;
    @Nullable
    private static KeyBinding menuKeyBinding;

    private OxygenMenuHelper() {}

    public static Map<Integer, OxygenMenuEntry> getMenuEntriesMap() {
        return MENU_ENTRIES_MAP;
    }

    public static void addMenuEntry(OxygenMenuEntry entry) {
        MENU_ENTRIES_MAP.put(entry.getScreenId(), entry);
    }

    public static boolean isMenuEnabled() {
        return menuEnabled;
    }

    public static void setMenuEnabled(boolean flag) {
        menuEnabled = flag;
    }

    @Nullable
    public static KeyBinding getMenuKeyBinding() {
        return menuKeyBinding;
    }

    public static void setMenuKeyBinding(KeyBinding keyBinding) {
        menuKeyBinding = keyBinding;
    }
}
