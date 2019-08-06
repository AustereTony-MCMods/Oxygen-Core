package austeretony.oxygen.common.sync.gui.api;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.api.network.OxygenNetwork;
import net.minecraft.network.PacketBuffer;

public interface IComplexGUIHandlerServer {

    OxygenNetwork getNetwork();

    Set<Long> getValidIdentifiersFirst(UUID playerUUID);

    Set<Long> getValidIdentifiersSecond(UUID playerUUID);

    void writeEntries(UUID playerUUID, PacketBuffer buffer, long[] firstIds, long[] secondIds);
}
