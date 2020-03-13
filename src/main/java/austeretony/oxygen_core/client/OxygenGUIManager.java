package austeretony.oxygen_core.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.gui.elements.OxygenContextMenu.OxygenContextMenuAction;
import austeretony.oxygen_core.client.gui.notifications.NotificationsScreen;
import austeretony.oxygen_core.client.gui.overlay.Overlay;
import austeretony.oxygen_core.client.gui.privileges.information.PrivilegesScreen;
import austeretony.oxygen_core.client.gui.privileges.management.PrivilegesManagementScreen;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.common.util.ArrayListWrapper;

public class OxygenGUIManager {

    private final Map<Integer, ArrayListWrapper<OxygenContextMenuAction>> contextActionsRegistry = new HashMap<>(10);

    private final Set<Overlay> overlays = new HashSet<>(5);

    public void registerScreenId(int screenId) {
        if (!this.contextActionsRegistry.containsKey(screenId))
            this.contextActionsRegistry.put(screenId, new ArrayListWrapper<>());
    }

    public void registerContextAction(int screenId, OxygenContextMenuAction action) {
        if (!this.contextActionsRegistry.containsKey(screenId))
            this.registerScreenId(screenId);
        this.contextActionsRegistry.get(screenId).add(action);
    }

    public List<OxygenContextMenuAction> getContextActions(int screenId) {
        return this.contextActionsRegistry.get(screenId).list;
    }

    public void registerOverlay(Overlay overlay) {
        this.overlays.add(overlay);
    }

    public Set<Overlay> getOverlays() {
        return this.overlays;
    }

    public static void openNotificationsMenu() {
        ClientReference.displayGuiScreen(new NotificationsScreen());
    }

    public static void openSettingsMenu() {
        ClientReference.displayGuiScreen(new SettingsScreen());
    }

    public static void openPrivilegesMenu() {
        ClientReference.displayGuiScreen(new PrivilegesScreen());
    }

    public static void openPrivilegesManagementMenu() {
        ClientReference.displayGuiScreen(new PrivilegesManagementScreen());
    }
}
