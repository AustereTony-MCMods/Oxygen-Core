package austeretony.oxygen_core.client.api;

import java.util.Set;
import java.util.TreeSet;

import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;

public class OxygenMenuHelper {

    private static boolean oxygenMenuEnabled;

    private static final Set<OxygenMenuEntry> MENU_ENTRIES = new TreeSet<>((e1, e2)->e1.getLocalizedName().compareTo(e2.getLocalizedName()));

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
