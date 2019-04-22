package austeretony.oxygen.common.core.api;

import java.util.UUID;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientReference {

    public static void registerKeyBinding(KeyBinding keyBinding) {
        ClientRegistry.registerKeyBinding(keyBinding);		
    }

    public static void registerCommand(ICommand command) {
        ClientCommandHandler.instance.registerCommand(command);          
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayer getClientPlayer() {
        return getMinecraft().player;
    }

    public static NetworkPlayerInfo getPlayerInfo(UUID playerUUID) {
        return ((EntityPlayerSP) getClientPlayer()).connection.getPlayerInfo(playerUUID);
    }

    public static boolean hasActiveGUI() {
        return !getMinecraft().inGameHasFocus;
    }

    public static void displayGuiScreen(GuiScreen guiScreen) {
        getMinecraft().displayGuiScreen(guiScreen);
    }

    public static void showMessage(ITextComponent message) {
        getClientPlayer().sendMessage(message);
    }

    public static void showMessage(String message, Object ... args) {
        showMessage(new TextComponentTranslation(message, args));
    }
}
