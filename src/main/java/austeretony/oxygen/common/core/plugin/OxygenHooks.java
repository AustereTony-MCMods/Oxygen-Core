package austeretony.oxygen.common.core.plugin;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenPlayerData;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;

public class OxygenHooks {

    //Hook to <net.minecraft.client.gui.GuiPlayerTabOverlay> class to <renderPlayerlist()> method.
    public static void verifyPlayersList(List<NetworkPlayerInfo> infoList) {
        Iterator<NetworkPlayerInfo> iterator = infoList.iterator();
        UUID playerUUID;
        while (iterator.hasNext()) {
            playerUUID = iterator.next().getGameProfile().getId();          
            if (OxygenHelperClient.isOnline(playerUUID)//check data synced 
                    && OxygenHelperClient.isOfflineStatus(playerUUID))
                iterator.remove();
        }
    }

    //Hook to <net.minecraft.network.NetHandlerPlayServer> class to <processChatMessage()> method.
    public static void processChatMessage(EntityPlayerMP playerMP, String rawMessage, ITextComponent formatted) {
        UUID senderUUID = CommonReference.uuid(playerMP);
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue()) {
            IPrivilegedGroup group = PrivilegeProviderServer.getPlayerGroup(senderUUID);
            StringBuilder username = new StringBuilder();
            if (!group.getPrefix().isEmpty()) {
                username.append(group.getPrefixColor());
                username.append("[");
                username.append(group.getPrefix());
                username.append("]");
                username.append(TextFormatting.RESET);
            }
            username.append(group.getUsernameColor());
            username.append(CommonReference.username(playerMP));
            username.append(TextFormatting.RESET);
            if (!group.getSuffix().isEmpty()) {
                username.append(group.getSuffixColor());
                username.append("(");
                username.append(group.getSuffix());
                username.append(")");
                username.append(TextFormatting.RESET);
            }
            formatted = new TextComponentTranslation("chat.type.text", username.toString(), ForgeHooks.newChatWithLinks(group.getChatColor() + rawMessage));
        }

        //Prevents sending chat messages to players, which ignores sender
        //TODO Probably better filter out messages on client
        OxygenPlayerData recieverData;
        CommonReference.getServer().sendMessage(formatted);
        for (EntityPlayerMP recieverMP : CommonReference.getServer().getPlayerList().getPlayers()) {
            recieverData = OxygenHelperServer.getPlayerData(CommonReference.uuid(recieverMP));
            if (!recieverData.haveFriendListEntryForUUID(senderUUID) || (recieverData.haveFriendListEntryForUUID(senderUUID) && !recieverData.getFriendListEntryByUUID(senderUUID).ignored))
                recieverMP.connection.sendPacket(new SPacketChat(formatted, ChatType.CHAT));
        }
    }

    //Hook to <net.minecraft.client.gui.GuiIngame> class to <renderGameOverlay()> method.
    //Hook to <net.minecraftforge.client.GuiIngameForge> class to <renderPlayerList()> method.
    public static boolean renderTabOverlay() {
        return !OxygenConfig.DISABLE_TAB_OVERLAY.getBooleanValue();
    }
}
