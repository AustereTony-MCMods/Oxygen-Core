package austeretony.oxygen_core.common.network.packets.server;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

import java.util.UUID;

public class SPRequestAction extends Packet {

    private UUID requestUUID;
    private boolean accepted;

    public SPRequestAction() {}

    public SPRequestAction(UUID requestUUID, boolean accepted) {
        this.requestUUID = requestUUID;
        this.accepted = accepted;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        ByteBufUtils.writeUUID(requestUUID, buffer);
        buffer.writeBoolean(accepted);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenServer.isNetworkRequestAvailable(OxygenMain.NET_REQUEST_REQUEST_ACTION, MinecraftCommon.getEntityUUID(playerMP))) {
            final UUID requestUUID = ByteBufUtils.readUUID(buffer);
            final boolean accepted = buffer.readBoolean();

            if (accepted) {
                OxygenServer.addTask(() -> OxygenManagerServer.instance().getNotificationsManager()
                        .acceptRequest(playerMP, requestUUID));
            } else {
                OxygenServer.addTask(() -> OxygenManagerServer.instance().getNotificationsManager()
                        .rejectRequest(playerMP, requestUUID));
            }
        }
    }
}
