package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.sync.gui.api.AdvancedGUIHandlerClient;
import austeretony.oxygen.client.sync.gui.api.ComplexGUIHandlerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPOpenOxygenScreen extends ProxyPacket {

    private int type, screenId;

    public CPOpenOxygenScreen() {}

    public CPOpenOxygenScreen(EnumScreenType type, int screenId) {
        this.type = type.ordinal();
        this.screenId = screenId;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.type);
        buffer.writeByte(this.screenId);
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        switch (EnumScreenType.values()[buffer.readByte()]) {
        case SHARED_DATA_LISTENER_SCREEN:
            OxygenManagerClient.instance().getGUIManager().openSharedDataListenerScreenDelegated(buffer.readByte());
            break;
        case ADVANCED_SCREEN:
            AdvancedGUIHandlerClient.openScreenDelegated(buffer.readByte());
            break;
        case DOUBLE_ADVANCED_SCREEN:
            ComplexGUIHandlerClient.openScreenDelegated(buffer.readByte());
            break;
        }
    }
}
