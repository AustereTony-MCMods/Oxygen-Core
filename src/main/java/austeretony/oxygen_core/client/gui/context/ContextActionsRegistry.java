package austeretony.oxygen_core.client.gui.context;

import austeretony.oxygen_core.client.gui.base.context.ContextAction;

import java.util.*;

public class ContextActionsRegistry {

    private static final Map<Integer, List<ContextAction>> REGISTRY = new HashMap<>();

    public static void register(int screenId, ContextAction action) {
        List<ContextAction> actions = REGISTRY.computeIfAbsent(screenId, ArrayList::new);
        actions.add(action);
    }

    public static Map<Integer, List<ContextAction>> getRegistryMap() {
        return REGISTRY;
    }

    public static List<ContextAction> getContextActions(int screenId) {
        return REGISTRY.getOrDefault(screenId, Collections.emptyList());
    }
}
