package austeretony.oxygen.common.core.api;

import java.io.File;
import java.util.UUID;

import net.minecraft.command.ICommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.WorldServer;
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

    public static UUID getPersistentUUID(Entity entity) {
        return entity.getPersistentID();
    }

    public static String getName(Entity entity) {
        return entity.getName();
    }

    public static EntityPlayerMP playerByUUID(UUID playerUUID) {
        return getServer().getPlayerList().getPlayerByUUID(playerUUID);
    }

    public static EntityPlayerMP playerByUsername(String username) {
        return getServer().getPlayerList().getPlayerByUsername(username);
    }

    public static int getEntityId(Entity entity) {
        return entity.getEntityId();
    }

    public static Entity getEntityById(Entity reference, int entityId) {
        return reference.world.getEntityByID(entityId);
    }

    public static boolean isEntitiesNear(Entity first, Entity second, double range) {
        return first.getDistanceSq(second) <= range * range;
    }

    public static void teleportPlayer(EntityPlayerMP playerMP, int dimension, double x, double y, double z, float yaw, float pitch) {
        playerMP.rotationYaw = playerMP.rotationYawHead = yaw;
        playerMP.rotationPitch = pitch;    
        teleportPlayer(playerMP, dimension, x, y, z);
    }

    public static void teleportPlayer(EntityPlayerMP playerMP, int dimension, double x, double y, double z) {
        if (playerMP.dimension == dimension)                                                 
            playerMP.setPositionAndUpdate(x, y, z);    
        else                                                
            transferToDimension(playerMP, dimension, x, y, z);
    }

    public static void transferToDimension(EntityPlayerMP playerMP, int dimension, double x, double y, double z) {
        int currDimension = playerMP.dimension;
        WorldServer worldServer = getServer().getWorld(currDimension);
        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(playerMP, dimension, new SimpleTeleporter(worldServer, x, y, z));
        playerMP.setPositionAndUpdate(x, y, z);
        if (currDimension == 1) {
            worldServer.spawnEntity(playerMP);
            worldServer.updateEntityWithOptionalForce(playerMP, false);
        }
    }

    public static void delegateToServerThread(Runnable task) {
        getServer().addScheduledTask(task);
    }
}