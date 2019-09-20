package austeretony.oxygen_core.client.gui.menu;

import java.util.LinkedHashSet;
import java.util.Set;

public class OxygenMenuManager {

    private static boolean oxygenMenuEnabled;

    private static final Set<OxygenMenuEntry> MENU_ENTRIES = new LinkedHashSet<>(10);

    public static void enableOxygenMenu() {
        oxygenMenuEnabled = true;
    }

    public static boolean isOxygenMenuEnabled() {
        return oxygenMenuEnabled;
    }

    public static void registerOxygenMenuEntry(OxygenMenuEntry entry) {
        MENU_ENTRIES.add(entry);
    }

    public static Set<OxygenMenuEntry> getOxygenMenuEntries() {
        return MENU_ENTRIES;
    }
}
