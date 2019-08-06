package austeretony.oxygen.client.core.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.ResourceLocation;
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

    public static void showMessage(ITextComponent message) {
        getClientPlayer().sendMessage(message);
    }

    public static void showMessage(String message, Object... args) {
        showMessage(new TextComponentTranslation(message, args));
    }

    private static final Map<ResourceLocation, Boolean> VERIFIED_TEXTURES = new HashMap<ResourceLocation, Boolean>();

    public static boolean isTextureExist(ResourceLocation location) {
        if (!VERIFIED_TEXTURES.containsKey(location)) {
            IResource resource = null;
            try {
                resource = ClientReference.getMinecraft().getResourceManager().getResource(location);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Texture not found! Domain: {}, path: {}.", location.getResourceDomain(), location.getResourcePath());
            }
            VERIFIED_TEXTURES.put(location, resource != null);
            return resource != null;
        }
        return VERIFIED_TEXTURES.get(location);
    }

    public static void delegateToClientThread(Runnable task) {
        getMinecraft().addScheduledTask(task);
    }
}
