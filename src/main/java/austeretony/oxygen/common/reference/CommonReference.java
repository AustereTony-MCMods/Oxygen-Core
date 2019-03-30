package austeretony.oxygen.common.reference;

import java.io.File;

import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

public class CommonReference {

    public static String getGameFolder() {
        return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
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
}
