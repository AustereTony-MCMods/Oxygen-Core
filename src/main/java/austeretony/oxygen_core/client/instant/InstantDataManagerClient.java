package austeretony.oxygen_core.client.instant;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import austeretony.oxygen_core.common.instant.InstantData;
import io.netty.buffer.ByteBuf;

public class InstantDataManagerClient {

    private final Map<Integer, InstantData> registry = new HashMap<>();

    private final Map<Integer, InstantDataContainer> containers = new ConcurrentHashMap<>();

    public void registerInstantData(InstantData data) {
        this.registry.put(data.getIndex(), data);
    }

    public void dataReceived(ByteBuf buffer) {
        try {
            int 
            amount = buffer.readShort(),
            entityId,
            index,
            j;            
            InstantDataContainer container;
            for (int i = 0; i < amount; i++) {
                entityId = buffer.readInt();
                if (entityId != - 1) {
                    container = this.getInstantDataContainer(entityId);   
                    for (j = 0; j < this.registry.size(); j++) {
                        index = buffer.readByte();
                        if (index != - 1)
                            container.get(index).read(buffer);
                    }

                }
            }
        } finally {
            if (buffer != null)
                buffer.release();
        }
    }

    public InstantDataContainer getInstantDataContainer(int entityId) {
        InstantDataContainer container = this.containers.get(entityId);
        if (container == null) {
            container = new InstantDataContainer();
            for (InstantData data : this.registry.values())
                container.init(data.getIndex(), data.copy());
            this.containers.put(entityId, container);
        }  
        return container;
    }

    public InstantData getInstantData(int entityId, int index) {
        return this.getInstantDataContainer(entityId).get(index);
    }
}
