package austeretony.oxygen.common.main;

import austeretony.oxygen.client.reference.ClientReference;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum EnumChatMessages {

    SIMPLE_LINE,
    COMMAND_PRIVILEGE_INFO_GROUP_INFO,
    COMMAND_PRIVILEGE_INFO_GROUP_PRIVILEGES,
    COMMAND_PRIVILEGE_INFO_GROUPS_LIST,
    COMMAND_PRIVILEGE_INFO_PLAYER_GROUP,
    COMMAND_PRIVILEGE_GROUP_CREATE,
    COMMAND_PRIVILEGE_GROUP_EDIT,
    COMMAND_PRIVILEGE_GROUP_REMOVE,
    COMMAND_PRIVILEGE_PRIVILEGE_ADD,
    COMMAND_PRIVILEGE_PRIVILEGE_REMOVE,
    COMMAND_PRIVILEGE_PROMOTE;

    @SideOnly(Side.CLIENT)
    public void show(String... args) {
        switch (this) {
        case SIMPLE_LINE:
            ClientReference.showMessage(new TextComponentString(args[0]));
            break;
        case COMMAND_PRIVILEGE_INFO_GROUP_INFO:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.info.groupInfo", args[0], args[1], args[2], args[3], args[4], args[5], args[6], args[7]));
            break;
        case COMMAND_PRIVILEGE_INFO_GROUP_PRIVILEGES:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.info.groupPrivileges", args[0]));
            break;
        case COMMAND_PRIVILEGE_INFO_GROUPS_LIST:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.info.groupsList"));
            break;
        case COMMAND_PRIVILEGE_INFO_PLAYER_GROUP:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.info.playerGroup", args[0], args[1]));
            break;
        case COMMAND_PRIVILEGE_GROUP_CREATE:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.group.create", args[0]));
            break;
        case COMMAND_PRIVILEGE_GROUP_EDIT:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.group.edit", args[0]));
            break;
        case COMMAND_PRIVILEGE_GROUP_REMOVE:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.group.remove", args[0]));
            break;
        case COMMAND_PRIVILEGE_PRIVILEGE_ADD:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.privilege.add", args[0], args[1], args[2]));
            break;
        case COMMAND_PRIVILEGE_PRIVILEGE_REMOVE:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.privilege.remove", args[0], args[1]));
            break;
        case COMMAND_PRIVILEGE_PROMOTE:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.command.privilege.promote", args[0], args[1]));
            break;
        }
    }
}
