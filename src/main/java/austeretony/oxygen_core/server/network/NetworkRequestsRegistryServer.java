package austeretony.oxygen_core.server.network;

import java.util.HashSet;
import java.util.Set;

public class NetworkRequestsRegistryServer {

    public static final Set<RegistryData> REGISTRY = new HashSet<>(); 

    public static void registerRequest(int id, int cooldownMillis) {
        REGISTRY.add(new RegistryData(id, cooldownMillis));
    }

    public static class RegistryData {

        public final int id;

        public final int cooldownMillis;

        private RegistryData(int id, int cooldownMillis) {
            this.id = id;
            this.cooldownMillis = cooldownMillis;
        }
    }
}
