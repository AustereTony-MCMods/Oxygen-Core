package austeretony.oxygen_core.client.instant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.instant.InstantData;

public class InstantDataContainer {

    private final Map<Integer, InstantData> data = new ConcurrentHashMap<>();

    public void init(int index, InstantData data) {
        this.data.put(index, data);
    }

    public InstantData get(int index) {
        return this.data.get(index);
    }
}
