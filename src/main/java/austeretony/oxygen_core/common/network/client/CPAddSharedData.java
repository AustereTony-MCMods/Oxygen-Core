package austeretony.oxygen_core.common.network.client;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.PlayerSharedData;
import austeretony.oxygen_core.common.network.Packet;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.INetHandler;

public class CPAddSharedData extends Packet {

    private PlayerSharedData sharedData;

    public CPAddSharedData() {}

    public CPAddSharedData(PlayerSharedData sharedData) {
        this.sharedData = sharedData;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        this.sharedData.write(buffer);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final PlayerSharedData sharedData = PlayerSharedData.read(buffer);
        OxygenHelperClient.addRoutineTask(()->OxygenManagerClient.instance().getSharedDataManager().addSharedData(sharedData));
    }
}
