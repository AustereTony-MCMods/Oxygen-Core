package austeretony.oxygen_core.server.event;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.PrivilegeProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenPrivilegesLoadedEvent;
import austeretony.oxygen_core.server.config.OxygenConfigServer;
import austeretony.oxygen_core.server.config.PrivilegesConfig;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;

public class OxygenEventsServer {

    @SubscribeEvent
    public void onPrivilegesLoaded(OxygenPrivilegesLoadedEvent event) {
        OxygenMain.addDefaultPrivileges();
    }

    @SubscribeEvent
    public void onPlayerLogIn(PlayerLoggedInEvent event) {        
        OxygenManagerServer.instance().playerLoggedIn((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerLoggedOutEvent event) {
        OxygenManagerServer.instance().playerLoggedOut((EntityPlayerMP) event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        OxygenManagerServer.instance().playerChangedDimension((EntityPlayerMP) event.player, event.fromDim, event.toDim);
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (OxygenConfigServer.ENABLE_PRIVILEGES.getBooleanValue() 
                && PrivilegesConfig.ENABLE_FORMATTED_CHAT.getBooleanValue()) {
            UUID senderUUID = CommonReference.getPersistentUUID(event.getPlayer());
            PrivilegedGroup group = PrivilegeProviderServer.getPlayerGroup(senderUUID);
            StringBuilder username = new StringBuilder();
            if (!group.getPrefix().isEmpty()) {
                username.append(group.getPrefixColor());
                username.append("[");
                username.append(group.getPrefix());
                username.append("]");
                username.append(TextFormatting.RESET);
            }
            username.append(group.getUsernameColor());
            username.append(CommonReference.getName(event.getPlayer()));
            username.append(TextFormatting.RESET);
            if (!group.getSuffix().isEmpty()) {
                username.append(group.getSuffixColor());
                username.append("(");
                username.append(group.getSuffix());
                username.append(")");
                username.append(TextFormatting.RESET);
            }
            event.setComponent(new TextComponentTranslation("chat.type.text", username.toString(), ForgeHooks.newChatWithLinks(group.getChatColor() + event.getMessage())));
        }
    }
}
