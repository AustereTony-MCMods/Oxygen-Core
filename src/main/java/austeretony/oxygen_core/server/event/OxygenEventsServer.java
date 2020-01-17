package austeretony.oxygen_core.server.event;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.config.PrivilegesConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.PrivilegesProviderServer;
import austeretony.oxygen_core.server.api.event.OxygenPrivilegesLoadedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
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
    public void onPlayerStartTracking(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof EntityPlayer)//TODO 0.10 - make it applicable for all EntityLivingBase when an Damage Overlay module will be created
            OxygenManagerServer.instance().playerStartTracking((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
    }

    @SubscribeEvent
    public void onPlayerStopTracking(PlayerEvent.StopTracking event) {
        if (event.getTarget() instanceof EntityPlayer)//TODO 0.10 - make it applicable for all EntityLivingBase when an Damage Overlay module will be created
            OxygenManagerServer.instance().playerStopTracking((EntityPlayerMP) event.getEntityPlayer(), event.getTarget());
    }

    @SubscribeEvent
    public void onChatMessage(ServerChatEvent event) {
        if (OxygenConfig.ENABLE_PRIVILEGES.asBoolean() 
                && PrivilegesConfig.ENABLE_FORMATTED_CHAT.asBoolean()) {
            UUID playerUUID = CommonReference.getPersistentUUID(event.getPlayer());
            Role role = PrivilegesProviderServer.getChatFormattingPlayerRole(playerUUID);
            if (role != null) {
                if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                    String prefix = null, username, result;

                    TextFormatting defColor = TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()];

                    if (PrivilegesConfig.SHOW_PREFIX.asBoolean() && !role.getPrefix().isEmpty())
                        prefix = role.getPrefixColor() + role.getPrefix() + TextFormatting.RESET;

                    username = role.getUsernameColor() + CommonReference.getName(event.getPlayer()) + TextFormatting.RESET;

                    result = !role.getPrefix().isEmpty() ? PrivilegesConfig.FORMATTED_CHAT_PREFIX_PATTERN.asString() : PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString();
                    if (prefix != null)
                        result = defColor + result.replace("@prefix", prefix + defColor);
                    result = result.replace("@username", username + defColor);

                    ITextComponent messageComponent = ForgeHooks.newChatWithLinks(event.getMessage());
                    messageComponent.getStyle().setColor(role.getChatColor());

                    event.setComponent(new TextComponentString(result).appendSibling(messageComponent));
                } else {
                    StringBuilder formattedUsername = new StringBuilder();

                    if (PrivilegesConfig.SHOW_PREFIX.asBoolean() && !role.getPrefix().isEmpty()) {
                        formattedUsername.append(role.getPrefixColor());
                        formattedUsername.append("[");
                        formattedUsername.append(role.getPrefix());
                        formattedUsername.append("]");
                        formattedUsername.append(TextFormatting.RESET);
                    }

                    formattedUsername.append(role.getUsernameColor());
                    formattedUsername.append(CommonReference.getName(event.getPlayer()));
                    formattedUsername.append(TextFormatting.RESET);

                    ITextComponent messageComponent = ForgeHooks.newChatWithLinks(event.getMessage());
                    messageComponent.getStyle().setColor(role.getChatColor());

                    event.setComponent(new TextComponentTranslation("chat.type.text", formattedUsername.toString(), messageComponent));
                }
            } else if (PrivilegesConfig.ENABLE_CUSTOM_FORMATTED_CHAT.asBoolean()) {
                ITextComponent messageComponent = new TextComponentString(PrivilegesConfig.FORMATTED_CHAT_PATTERN.asString().replace("@username", CommonReference.getName(event.getPlayer())))
                        .appendSibling(ForgeHooks.newChatWithLinks(event.getMessage()));
                messageComponent.getStyle().setColor(TextFormatting.values()[PrivilegesConfig.DEFAULT_CHAT_COLOR.asInt()]);
                event.setComponent(messageComponent);
            }
        }
    }
}
