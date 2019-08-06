package austeretony.oxygen.common.sync.gui.api;

import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.api.network.OxygenNetwork;
import net.minecraft.network.PacketBuffer;

public interface IAdvancedGUIHandlerServer {

    OxygenNetwork getNetwork();

    Set<Long> getValidIdentifiers(UUID playerUUID);

    void writeEntries(UUID playerUUID, PacketBuffer buffer, long[] entriesIds);
}
