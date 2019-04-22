package austeretony.oxygen.common.network.client;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.common.network.ProxyPacket;
import net.minecraft.network.INetHandler;
import net.minecraft.network.PacketBuffer;

public class CPCommand extends ProxyPacket {

    private EnumCommand command;

    public CPCommand() {}

    public CPCommand(EnumCommand command) {
        this.command = command;
    }

    @Override
    public void write(PacketBuffer buffer, INetHandler netHandler) {
        buffer.writeByte(this.command.ordinal());
    }

    @Override
    public void read(PacketBuffer buffer, INetHandler netHandler) {
        this.command = EnumCommand.values()[buffer.readByte()];
        switch (this.command) {
        case OPEN_FRIENDS_LIST:
            OxygenManagerClient.instance().getFriendListManager().openFriendsListDelegated();
            break;
        case OPEN_PLAYERS_LIST:
            OxygenManagerClient.instance().openPlayersListDelegated();
            break;
        case OPEN_INTERACT_PLAYER_MENU:
            OxygenManagerClient.instance().getInteractionManager().openPlayerInteractionMenuDelegated();
            break;
        }
    }

    public enum EnumCommand {

        OPEN_FRIENDS_LIST,
        OPEN_PLAYERS_LIST,
        OPEN_INTERACT_PLAYER_MENU
    }
}
