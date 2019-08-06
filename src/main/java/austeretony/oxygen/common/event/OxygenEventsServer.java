package austeretony.oxygen.common.event;

import java.util.UUID;

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.event.OxygenPrivilegesLoadedEvent;
import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegeProviderServer;
import austeretony.oxygen.common.privilege.config.OxygenPrivilegeConfig;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;

public class OxygenEventsServer {

    @SubscribeEvent
    public void onPrivilegesLoaded(OxygenPrivilegesLoadedEvent event) {
        OxygenMain.addDefaultPrivileges();
    }

    @SubscribeEvent
    public void onPlayerLogIn(PlayerLoggedInEvent event) {        
        OxygenManagerServer.instance().onPlayerLoggedIn(event.player);
    }

    @SubscribeEvent
    public void onPlayerLogOut(PlayerLoggedOutEvent event) {
        OxygenManagerServer.instance().onPlayerLoggedOut(event.player);
    }

    @SubscribeEvent
    public void onPlayerChangedDimension(PlayerChangedDimensionEvent event) {
        OxygenManagerServer.instance().onPlayerChangedDimension(event.player, event.fromDim, event.toDim);
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (OxygenConfig.ENABLE_PRIVILEGES.getBooleanValue() 
                && OxygenPrivilegeConfig.ENABLE_FORMATTED_CHAT.getBooleanValue()) {
            UUID senderUUID = CommonReference.getPersistentUUID(event.getPlayer());
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

    @SubscribeEvent
    public void onServerTick(ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.START)
            OxygenManagerServer.instance().getProcessesManager().runProcesses();
    }
}
