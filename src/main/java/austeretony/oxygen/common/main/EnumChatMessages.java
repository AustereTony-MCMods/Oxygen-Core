package austeretony.oxygen.common.main;

import austeretony.oxygen.common.core.api.ClientReference;
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
    COMMAND_PRIVILEGE_PROMOTE,
    STATUS_CHANGED,
    REQUEST_RESET,
    TARGET_OFFLINE,
    SENDER_IGNORED,
    FRIEND_REQUEST_ACCEPTED_SENDER,
    FRIEND_REQUEST_ACCEPTED_TARGET,
    FRIEND_REQUEST_REJECTED_SENDER,
    FRIEND_REQUEST_REJECTED_TARGET,
    FRIEND_REMOVED,
    ADDED_TO_IGNORED,
    NOTE_EDITED,
    IGNORED_REMOVED;

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
        case STATUS_CHANGED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.statusChanged", args[0]));
            break;
        case REQUEST_RESET:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.requestReset"));
            break;
        case TARGET_OFFLINE:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.targetOffline"));
            break;
        case SENDER_IGNORED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.senderIgnored"));
            break;
        case FRIEND_REQUEST_ACCEPTED_SENDER:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.senderFriendRequestAccepted"));
            break;
        case FRIEND_REQUEST_ACCEPTED_TARGET:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.targetFriendRequestAccepted"));
            break;
        case FRIEND_REQUEST_REJECTED_SENDER:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.senderFriendRequestRejected"));
            break;
        case FRIEND_REQUEST_REJECTED_TARGET:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.targetFriendRequestRejected"));
            break;
        case FRIEND_REMOVED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.friendRemoved"));
            break;
        case ADDED_TO_IGNORED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.addedToIgnored"));
            break;
        case NOTE_EDITED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.noteEdited"));
            break;
        case IGNORED_REMOVED:
            ClientReference.showMessage(new TextComponentTranslation("oxygen.message.ignoredRemoved"));
            break;
        }
    }
}
