package austeretony.oxygen.client.gui;

import java.util.LinkedHashSet;
import java.util.Set;

public class OxygenMenuManager {

    private static boolean oxygenMenuEnabled;

    private static final Set<AbstractMenuEntry> MENU_ENTRIES = new LinkedHashSet<AbstractMenuEntry>(10);

    public static void enableOxygenMenu() {
        oxygenMenuEnabled = true;
    }

    public static boolean isOxygenMenuEnabled() {
        return oxygenMenuEnabled;
    }

    public static void registerOxygenMenuEntry(AbstractMenuEntry entry) {
        MENU_ENTRIES.add(entry);
    }

    public static Set<AbstractMenuEntry> getOxygenMenuEntries() {
        return MENU_ENTRIES;
    }
}
