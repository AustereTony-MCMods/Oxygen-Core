package austeretony.oxygen_core.common.network.server;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.Packet;
import austeretony.oxygen_core.common.util.ByteBufUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetHandler;

public class SPCreateRole extends Packet {

    private int roleId, roleNameColor, prefixColor, usernameColor, chatColor;

    private String roleName, prefix;

    public SPCreateRole() {}

    public SPCreateRole(int roleId, String roleName, String prefix, int roleNameColor, int prefixColor, int usernameColor, int chatColor) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.prefix = prefix;
        this.roleNameColor = roleNameColor;
        this.prefixColor = prefixColor;
        this.usernameColor = usernameColor;
        this.chatColor = chatColor;
    }

    @Override
    public void write(ByteBuf buffer, INetHandler netHandler) {
        buffer.writeByte(this.roleId);
        ByteBufUtils.writeString(this.roleName, buffer);
        ByteBufUtils.writeString(this.prefix, buffer);
        buffer.writeByte(this.roleNameColor);
        buffer.writeByte(this.prefixColor);
        buffer.writeByte(this.usernameColor);
        buffer.writeByte(this.chatColor);
    }

    @Override
    public void read(ByteBuf buffer, INetHandler netHandler) {
        final EntityPlayerMP playerMP = getEntityPlayerMP(netHandler);
        if (OxygenHelperServer.isNetworkRequestAvailable(CommonReference.getPersistentUUID(playerMP), OxygenMain.MANAGE_PRIVILEGES_REQUEST_ID)) {
            final int roleId = buffer.readByte();
            final String 
            roleName = ByteBufUtils.readString(buffer),
            prefix = ByteBufUtils.readString(buffer);
            final int 
            roleNameColor = buffer.readByte(),
            prefixColor = buffer.readByte(),
            usernameColor = buffer.readByte(),
            chatColor = buffer.readByte();
            OxygenHelperServer.addRoutineTask(()->OxygenManagerServer.instance().getPrivilegesManager().createRole(
                    playerMP,
                    roleId,
                    roleName,
                    prefix,
                    roleNameColor,
                    prefixColor,
                    usernameColor,
                    chatColor));
        }
    }
}
