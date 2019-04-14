package austeretony.oxygen.common.core.api;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientReference {

    public static void registerKeyBinding(KeyBinding keyBinding) {
        ClientRegistry.registerKeyBinding(keyBinding);		
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static EntityPlayer getClientPlayer() {
        return getMinecraft().player;
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
