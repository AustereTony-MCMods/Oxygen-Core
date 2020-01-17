package austeretony.oxygen_core.server.instant;

import java.util.ArrayList;
import java.util.List;

import austeretony.oxygen_core.common.instant.InstantData;

public class InstantDataRegistryServer {

    public static final List<InstantData> REGISTRY = new ArrayList<>(5);

    public static void registerInstantData(InstantData data) {
        REGISTRY.add(data);
    }
}
