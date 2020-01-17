package austeretony.oxygen_core.server;

import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.watcher.WatchedValue;

public class WatchedValuesRegistryServer {

    public static final Set<WatchedValue> REGISTRY = new HashSet<>(5);

    public static void registerWatchedValue(WatchedValue value) {
        REGISTRY.add(value);
    }
}