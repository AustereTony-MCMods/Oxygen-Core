package austeretony.oxygen_core.client.api;

import java.util.UUID;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientReference {

    public static void registerKeyBinding(KeyBinding keyBinding) {
        ClientRegistry.registerKeyBinding(keyBinding);		
    }

    public static void registerCommand(ICommand command) {
        ClientCommandHandler.instance.registerCommand(command);          
    }

    public static String localize(String key, Object... args) {
        return I18n.format(key, args);
    }

    public static Minecraft getMinecraft() {
        return Minecraft.getMinecraft();
    }

    public static GameSettings getGameSettings() {
        return getMinecraft().gameSettings;
    }

    public static World getClientWorld() {
        return getMinecraft().world;
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

    public static GuiScreen getCurrentScreen() {
        return getMinecraft().currentScreen;
    }

    public static IInventory getPlayerInventory() {
        return getClientPlayer().inventory;
    }

    public static Container getPlayerContainer() {
        return getClientPlayer().inventoryContainer;
    }

    public static Container getOpenContainer() {
        return getClientPlayer().openContainer;
    }

    public static Entity getPointedEntity() {
        return getMinecraft().pointedEntity;
    }

    public static Entity getEntityById(int entityId) {
        return getClientWorld().getEntityByID(entityId);
    }

    public static int getEntityId(Entity entity) {
        return entity.getEntityId();
    }

    public static UUID getPersistentUUID(Entity entity) {
        return entity.getPersistentID();
    }

    public static boolean isEntitiesNear(Entity first, Entity second, double range) {
        return first.getDistanceSq(second) <= range * range;
    }

    public static void showChatMessage(ITextComponent message) {
        getClientPlayer().sendMessage(message);
    }

    public static void showChatMessage(String message, Object... args) {
        showChatMessage(new TextComponentTranslation(message, args));
    }

    public static ListenableFuture<?> delegateToClientThread(Runnable task) {
        return getMinecraft().addScheduledTask(task);
    }
}
