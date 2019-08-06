package austeretony.oxygen.common.sync.gui.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.sync.gui.network.CPSyncAbsentEntries;
import austeretony.oxygen.common.sync.gui.network.CPSyncValidIdentifiers;
import austeretony.oxygen.common.sync.gui.network.SPSendAbsentEntriesIds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class AdvancedGUIHandlerServer {

    private static final Map<Integer, IAdvancedGUIHandlerServer> HANDLERS = new HashMap<Integer, IAdvancedGUIHandlerServer>(3);

    public static void registerScreen(int screenId, IAdvancedGUIHandlerServer handler) {
        HANDLERS.put(screenId, handler);
    }

    public static void init() {
        for (IAdvancedGUIHandlerServer handler : HANDLERS.values())
            registerPackets(handler.getNetwork());
    }

    private static void registerPackets(OxygenNetwork network) {
        network.registerPacketCheckExisted(CPSyncValidIdentifiers.class);
        network.registerPacketCheckExisted(SPSendAbsentEntriesIds.class);
        network.registerPacketCheckExisted(CPSyncAbsentEntries.class);
    }

    public static void openScreen(EntityPlayerMP playerMP, int screenId) {
        OxygenManagerServer.instance().openAdvancedScreen(playerMP, screenId);
    }

    public static OxygenNetwork getNetwork(int screenId) {
        return HANDLERS.get(screenId).getNetwork();
    }

    public static Set<Long> getValidIdentifiers(int screenId, UUID playerUUID) {
        return HANDLERS.get(screenId).getValidIdentifiers(playerUUID);
    }

    public static void writeEntries(int screenId, UUID playerUUID, PacketBuffer buffer, long[] entriesIds) {
        HANDLERS.get(screenId).writeEntries(playerUUID, buffer, entriesIds);
    }
}
