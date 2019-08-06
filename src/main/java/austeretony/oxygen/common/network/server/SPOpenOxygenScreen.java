package austeretony.oxygen.common.network.server;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.network.ProxyPacket;
import austeretony.oxygen.common.sync.gui.EnumScreenType;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class SPOpenOxygenScreen extends ProxyPacket {

    private int type, screenId;

    public SPOpenOxygenScreen() {}

    public SPOpenOxygenScreen(EnumScreenType type, int screenId) {
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
            OxygenManagerServer.instance().openSharedDataListenerScreen(getEntityPlayerMP(netHandler), buffer.readByte());
            break;
        case ADVANCED_SCREEN:
            OxygenManagerServer.instance().openAdvancedScreen(getEntityPlayerMP(netHandler), buffer.readByte());
            break;
        case DOUBLE_ADVANCED_SCREEN:
            OxygenManagerServer.instance().openComplexScreen(getEntityPlayerMP(netHandler), buffer.readByte());
            break;
        }
    }
}
