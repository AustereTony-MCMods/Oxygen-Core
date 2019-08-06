package austeretony.oxygen.client.sync.gui.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOpenOxygenScreen;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.common.sync.gui.network.CPSyncAbsentEntries;
import austeretony.oxygen.common.sync.gui.network.CPSyncValidIdentifiers;
import austeretony.oxygen.common.sync.gui.network.SPSendAbsentEntriesIds;
import net.minecraft.network.PacketBuffer;

public class AdvancedGUIHandlerClient {

    private static final Map<Integer, IAdvancedGUIHandlerClient> HANDLERS = new HashMap<Integer, IAdvancedGUIHandlerClient>(3);

    public static void registerScreen(int screenId, IAdvancedGUIHandlerClient handler) {
        HANDLERS.put(screenId, handler);
    }

    public static void init() {
        for (IAdvancedGUIHandlerClient handler : HANDLERS.values())
            registerPackets(handler.getNetwork());
    }

    private static void registerPackets(OxygenNetwork network) {
        network.registerPacketCheckExisted(CPSyncValidIdentifiers.class);
        network.registerPacketCheckExisted(SPSendAbsentEntriesIds.class);
        network.registerPacketCheckExisted(CPSyncAbsentEntries.class);
    }

    public static void openScreen(int screenId) {
        OxygenMain.network().sendToServer(new SPOpenOxygenScreen(EnumScreenType.ADVANCED_SCREEN, screenId));
    }

    public static OxygenNetwork getNetwork(int screenId) {
        return HANDLERS.get(screenId).getNetwork();
    }

    public static Set<Long> getIdentifiers(int screenId) {
        return HANDLERS.get(screenId).getIdentifiers();
    }

    public static Object getEntry(int screenId, long entryId) {
        return HANDLERS.get(screenId).getEntry(entryId);
    }

    public static void clearData(int screenId) {
        HANDLERS.get(screenId).clearData();
    }

    public static void addValidEntry(int screenId, Object entry) {
        HANDLERS.get(screenId).addValidEntry(entry);
    }

    public static void readEntries(int screenId, PacketBuffer buffer, int amount) {
        HANDLERS.get(screenId).readEntries(buffer, amount);
    }

    public static void openScreenDelegated(int screenId) {
        ClientReference.delegateToClientThread(()->HANDLERS.get(screenId).open());
    }
}
