package austeretony.oxygen_core.client.util;

import java.util.UUID;
import java.util.concurrent.Callable;

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
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import javax.annotation.Nullable;

public class MinecraftClient {

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

    public static EntityPlayer getPlayer() {
        return getMinecraft().player;
    }

    public static EntityPlayerSP getClientPlayer() {
        return getMinecraft().player;
    }

    @Nullable
    public static NetworkPlayerInfo getPlayerNetworkInfo(UUID playerUUID) {
        return getClientPlayer().connection.getPlayerInfo(playerUUID);
    }

    public static boolean hasActiveGUI() {
        return !getMinecraft().inGameHasFocus;
    }

    public static void displayGuiScreen(GuiScreen guiScreen) {
        getMinecraft().displayGuiScreen(guiScreen);
    }

    @Nullable
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

    @Nullable
    public static Entity getPointedEntity() {
        return getMinecraft().pointedEntity;
    }

    @Nullable
    public static Entity getEntityById(int entityId) {
        return getClientWorld().getEntityByID(entityId);
    }

    @Nullable
    public static EntityPlayer getPlayerByEntityId(int entityId) {
        Entity entity = getEntityById(entityId);
        if (entity instanceof EntityPlayer) {
            return (EntityPlayer) entity;
        }
        return null;
    }

    public static int getEntityId(Entity entity) {
        return entity.getEntityId();
    }

    public static UUID getEntityUUID(Entity entity) {
        return entity.getPersistentID();
    }

    public static String getEntityName(Entity entity) {
        return entity.getName();
    }

    public static float getDistanceBetween(Entity first, Entity second) {
        return first.getDistance(second);
    }

    public static void showChatMessage(ITextComponent message) {
        getClientPlayer().sendMessage(message);
    }

    public static void showChatMessage(String message, Object... args) {
        showChatMessage(new TextComponentTranslation(message, args));
    }

    public static void playSound(SoundEvent soundEvent, float volume, float pitch) {
        getClientPlayer().playSound(soundEvent, volume, pitch);
    }

    public static void playSound(SoundEvent soundEvent) {
        playSound(soundEvent, 0.5F, 1F);
    }

    public static void playUISound(SoundEvent soundEvent) {
        playSound(soundEvent, 0.4F, 1F);
    }

    public static ListenableFuture<?> delegateToClientThread(Runnable task) {
        return getMinecraft().addScheduledTask(task);
    }

    public static <T> ListenableFuture<T> delegateToClientThread(Callable<T> task) {
        return getMinecraft().addScheduledTask(task);
    }
}
