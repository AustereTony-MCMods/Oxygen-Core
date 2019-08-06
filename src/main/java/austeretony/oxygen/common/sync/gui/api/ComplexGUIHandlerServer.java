package austeretony.oxygen.common.sync.gui.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.sync.gui.network.CPComplexSyncAbsentEntries;
import austeretony.oxygen.common.sync.gui.network.CPComplexSyncValidIdentifiers;
import austeretony.oxygen.common.sync.gui.network.SPComplexSendAbsentEntriesIds;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;

public class ComplexGUIHandlerServer {

    private static final Map<Integer, IComplexGUIHandlerServer> HANDLERS = new HashMap<Integer, IComplexGUIHandlerServer>(3);

    public static void registerScreen(int screenId, IComplexGUIHandlerServer handler) {
        HANDLERS.put(screenId, handler);
    }

    public static void init() {
        for (IComplexGUIHandlerServer handler : HANDLERS.values())
            registerPackets(handler.getNetwork());
    }

    private static void registerPackets(OxygenNetwork network) {
        network.registerPacketCheckExisted(CPComplexSyncValidIdentifiers.class);
        network.registerPacketCheckExisted(SPComplexSendAbsentEntriesIds.class);
        network.registerPacketCheckExisted(CPComplexSyncAbsentEntries.class);
    }

    public static void openScreen(EntityPlayerMP playerMP, int screenId) {
        OxygenManagerServer.instance().openComplexScreen(playerMP, screenId);
    }

    public static OxygenNetwork getNetwork(int screenId) {
        return HANDLERS.get(screenId).getNetwork();
    }

    public static Set<Long> getValidIdentifiersFirst(int screenId, UUID playerUUID) {
        return HANDLERS.get(screenId).getValidIdentifiersFirst(playerUUID);
    }

    public static Set<Long> getValidIdentifiersSecond(int screenId, UUID playerUUID) {
        return HANDLERS.get(screenId).getValidIdentifiersSecond(playerUUID);
    }

    public static void writeEntries(int screenId, UUID playerUUID, PacketBuffer buffer, long[] firstIds, long[] secondIds) {
        HANDLERS.get(screenId).writeEntries(playerUUID, buffer, firstIds, secondIds);
    }
}
