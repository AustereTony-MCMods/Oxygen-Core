package austeretony.oxygen.common.reference;

import java.io.File;
import java.util.UUID;

import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class CommonReference {

    public static String getGameFolder() {
        return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
    }

    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static void registerEvent(Object event) {
        MinecraftForge.EVENT_BUS.register(event);
    }

    public static void registerCommand(FMLServerStartingEvent event, ICommand command) {
        event.registerServerCommand(command);
    }

    public static boolean isOpped(EntityPlayer player) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().canSendCommands(player.getGameProfile());
    }

    public static void sendMessage(EntityPlayer player, ITextComponent textComponent) {
        player.sendMessage(textComponent);
    }

    public static UUID uuid(EntityPlayer player) {
        return player.getGameProfile().getId();
    }

    public static String username(EntityPlayer player) {
        return player.getGameProfile().getName();
    }

    public static EntityPlayerMP playerByUUID(UUID playerUUID) {
        return getServer().getPlayerList().getPlayerByUUID(playerUUID);
    }

    public static EntityPlayerMP playerByUsername(String username) {
        return getServer().getPlayerList().getPlayerByUsername(username);
    }
}
