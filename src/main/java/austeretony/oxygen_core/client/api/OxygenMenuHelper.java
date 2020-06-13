package austeretony.oxygen_core.client.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import austeretony.oxygen_core.client.gui.menu.OxygenMenuEntry;

public class OxygenMenuHelper {

    private static boolean oxygenMenuEnabled;

    private static final List<OxygenMenuEntry> MENU_ENTRIES = new ArrayList<>();

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
        Collections.sort(MENU_ENTRIES, (e1, e2)->e1.getLocalizedName().compareTo(e2.getLocalizedName()));
        return new LinkedHashSet(MENU_ENTRIES);//TODO Get rid of this compatibility trick in 0.12
    }
}
