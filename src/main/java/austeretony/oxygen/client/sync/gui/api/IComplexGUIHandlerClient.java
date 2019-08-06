package austeretony.oxygen.client.sync.gui.api;

import java.util.Set;

import austeretony.oxygen.common.api.network.OxygenNetwork;
import net.minecraft.network.PacketBuffer;

public interface IComplexGUIHandlerClient<T, E> extends IGUIHandlerClient {

    OxygenNetwork getNetwork();

    Set<Long> getIdentifiersFirst();

    Set<Long> getIdentifiersSecond();

    T getEntryFirst(long entryId);

    E getEntrySecond(long entryId);

    void clearDataFirst();

    void clearDataSecond();

    void addValidEntryFirst(T entry);

    void addValidEntrySecond(E entry);

    void readEntries(PacketBuffer buffer, int firstAmount, int secondAmount);
}
