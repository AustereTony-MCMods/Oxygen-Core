package austeretony.oxygen.client.sync.gui.api;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.api.network.OxygenNetwork;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.network.server.SPOpenOxygenScreen;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import austeretony.oxygen.common.sync.gui.network.CPComplexSyncAbsentEntries;
import austeretony.oxygen.common.sync.gui.network.CPComplexSyncValidIdentifiers;
import austeretony.oxygen.common.sync.gui.network.SPComplexSendAbsentEntriesIds;
import net.minecraft.network.PacketBuffer;

public class ComplexGUIHandlerClient {

    private static final Map<Integer, IComplexGUIHandlerClient> HANDLERS = new HashMap<Integer, IComplexGUIHandlerClient>(3);

    public static void registerScreen(int screenId, IComplexGUIHandlerClient handler) {
        HANDLERS.put(screenId, handler);
    }

    public static void init() {
        for (IComplexGUIHandlerClient handler : HANDLERS.values())
            registerPackets(handler.getNetwork());
    }

    private static void registerPackets(OxygenNetwork network) {
        network.registerPacketCheckExisted(CPComplexSyncValidIdentifiers.class);
        network.registerPacketCheckExisted(SPComplexSendAbsentEntriesIds.class);
        network.registerPacketCheckExisted(CPComplexSyncAbsentEntries.class);
    }

    public static void openScreen(int screenId) {
        OxygenMain.network().sendToServer(new SPOpenOxygenScreen(EnumScreenType.COMPLEX_SCREEN, screenId));
    }

    public static OxygenNetwork getNetwork(int screenId) {
        return HANDLERS.get(screenId).getNetwork();
    }

    public static Set<Long> getIdentifiersFirst(int screenId) {
        return HANDLERS.get(screenId).getIdentifiersFirst();
    }

    public static Set<Long> getIdentifiersSecond(int screenId) {
        return HANDLERS.get(screenId).getIdentifiersSecond();
    }

    public static Object getEntryFirst(int screenId, long entryId) {
        return HANDLERS.get(screenId).getEntryFirst(entryId);
    }

    public static Object getEntrySecond(int screenId, long entryId) {
        return HANDLERS.get(screenId).getEntrySecond(entryId);
    }

    public static void clearDataFirst(int screenId) {
        HANDLERS.get(screenId).clearDataFirst();
    }

    public static void clearDataSecond(int screenId) {
        HANDLERS.get(screenId).clearDataSecond();
    }

    public static void addValidEntryFirst(int screenId, Object entry) {
        HANDLERS.get(screenId).addValidEntryFirst(entry);
    }

    public static void addValidEntrySecond(int screenId, Object entry) {
        HANDLERS.get(screenId).addValidEntrySecond(entry);
    }

    public static void readEntries(int screenId, PacketBuffer buffer, int firstAmount, int secondAmount) {
        HANDLERS.get(screenId).readEntries(buffer, firstAmount, secondAmount);
    }

    public static void openScreenDelegated(int screenId) {
        ClientReference.delegateToClientThread(()->HANDLERS.get(screenId).open());
    }
}
