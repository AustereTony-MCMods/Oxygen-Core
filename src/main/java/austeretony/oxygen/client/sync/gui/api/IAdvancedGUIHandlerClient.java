package austeretony.oxygen.client.sync.gui.api;

import java.util.Set;

import austeretony.oxygen.common.api.network.OxygenNetwork;
import net.minecraft.network.PacketBuffer;

public interface IAdvancedGUIHandlerClient<T> extends IGUIHandlerClient {

    OxygenNetwork getNetwork();

    Set<Long> getIdentifiers();

    T getEntry(long entryId);

    void clearData();

    void addValidEntry(T entry);

    void readEntries(PacketBuffer buffer, int amount);
}
