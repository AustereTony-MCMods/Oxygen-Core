package austeretony.oxygen_core.client.gui;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public final class OxygenGUIRegistry {

    private static final Map<Integer, GUIEntry> GUI_REGISTRY_MAP = new HashMap<>();

    private OxygenGUIRegistry() {}

    public static void registerGUI(int screenId, Runnable openGUITask, @Nullable Supplier<Boolean> accessSupplier) {
        GUI_REGISTRY_MAP.put(screenId, new GUIEntry(screenId, openGUITask, accessSupplier));
    }

    public static void registerGUI(int screenId, Runnable openGUITask) {
        registerGUI(screenId, openGUITask, null);
    }

    public static Map<Integer, GUIEntry> getGUIRegistryMap() {
        return GUI_REGISTRY_MAP;
    }

    public static class GUIEntry {

        private final int screenId;
        private final Runnable openGUITask;
        @Nullable
        private final Supplier<Boolean> accessSupplier;

        private GUIEntry(int screenId, Runnable openGUITask, @Nullable Supplier<Boolean> accessSupplier) {
            this.screenId = screenId;
            this.openGUITask = openGUITask;
            this.accessSupplier = accessSupplier;
        }

        public int getScreenId() {
            return screenId;
        }

        public Runnable getOpenGUITask() {
            return openGUITask;
        }

        @Nullable
        public Supplier<Boolean> getAccessSupplier() {
            return accessSupplier;
        }
    }
}
