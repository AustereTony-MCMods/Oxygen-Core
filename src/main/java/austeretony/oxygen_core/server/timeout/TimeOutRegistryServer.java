package austeretony.oxygen_core.server.timeout;

import java.util.HashSet;
import java.util.Set;

public class TimeOutRegistryServer {

    public static final Set<TimeOutRegistryEntry> REGISTRY = new HashSet<>(); 

    public static void registerTimeOut(int requestId, int timeOutMillis) {
        REGISTRY.add(new TimeOutRegistryEntry(requestId, timeOutMillis));
    }

    public static class TimeOutRegistryEntry {

        public final int id;

        public final int timeOutMillis;

        private TimeOutRegistryEntry(int id, int timeOutMillis) {
            this.id = id;
            this.timeOutMillis = timeOutMillis;
        }
    }
}
