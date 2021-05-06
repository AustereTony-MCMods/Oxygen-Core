package austeretony.oxygen_core.common.util;

import java.io.File;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Callable;

import com.google.common.util.concurrent.ListenableFuture;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.relauncher.FMLInjectionData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class MinecraftCommon {

    public static boolean isIDE() {
       return (Boolean) Launch.blackboard.get("fml.deobfuscatedEnvironment");
    }

    public static boolean isDedicatedServer() {
        return getServer().isDedicatedServer();
    }

    @Nonnull
    public static String getGameFolder() {
        return ((File) (FMLInjectionData.data()[6])).getAbsolutePath();
    }

    public static MinecraftServer getServer() {
        return FMLCommonHandler.instance().getMinecraftServerInstance();
    }

    public static int executeCommand(ICommandSender sender, String command) {
        return getServer().commandManager.executeCommand(sender, command);
    }

    public static int executeCommandConsole(String command) {
        return getServer().commandManager.executeCommand(getServer(), command);
    }

    public static void registerEventHandler(Object event) {
        MinecraftForge.EVENT_BUS.register(event);
    }

    public static boolean postEvent(Event event) {
        return MinecraftForge.EVENT_BUS.post(event);
    }

    public static void registerCommand(FMLServerStartingEvent event, ICommand command) {
        event.registerServerCommand(command);
    }

    public static List<EntityPlayerMP> getPlayers() {
        return getServer().getPlayerList().getPlayers();
    }

    public static boolean isPlayerOperator(EntityPlayer player) {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                .canSendCommands(player.getGameProfile());
    }

    public static void sendChatMessage(EntityPlayer player, ITextComponent textComponent) {
        player.sendMessage(textComponent);
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

    @Nullable
    public static EntityPlayerMP getPlayerByUUID(UUID playerUUID) {
        return getServer().getPlayerList().getPlayerByUUID(playerUUID);
    }

    @Nullable
    public static EntityPlayerMP getPlayerByUsername(String username) {
        return getServer().getPlayerList().getPlayerByUsername(username);
    }

    @Nullable
    public static EntityPlayerMP getPlayerByEntityId(int entityId) {
        Entity entity = getEntityById(entityId);
        if (entity instanceof EntityPlayerMP) {
            return (EntityPlayerMP) entity;
        }
        return null;
    }

    @Nullable
    public static Entity getEntityById(Entity reference, int entityId) {
        return reference.world.getEntityByID(entityId);
    }

    @Nullable
    public static Entity getEntityById(int entityId) {
        for (WorldServer world : getServer().worlds) {
            Entity entity = world.getEntityByID(entityId);
            if (entity != null) {
                return entity;
            }
        }
        return null;
    }

    @Nullable
    public static EntityLivingBase getEntityLivingBaseById(int entityId) {
        Entity entity = getEntityById(entityId);
        if (entity instanceof EntityLivingBase) {
            return (EntityLivingBase) entity;
        }
        return null;
    }

    public static float getDistanceBetween(Entity first, Entity second) {
        return first.getDistance(second);
    }

    public static void teleportPlayer(EntityPlayerMP playerMP, int dimension, double x, double y, double z, float yaw, float pitch) {
        playerMP.rotationYaw = playerMP.rotationYawHead = yaw;
        playerMP.rotationPitch = pitch;
        teleportPlayer(playerMP, dimension, x, y, z);
    }

    public static void teleportPlayer(EntityPlayerMP playerMP, int dimension, double x, double y, double z) {
        if (playerMP.dimension == dimension) {
            playerMP.setPositionAndUpdate(x, y, z);
        } else {
            transferToDimension(playerMP, dimension, x, y, z);
        }
    }

    public static void transferToDimension(EntityPlayerMP playerMP, int dimension, double x, double y, double z) {
        int currDimension = playerMP.dimension;
        WorldServer worldServer = getServer().getWorld(currDimension);
        worldServer.getMinecraftServer().getPlayerList().transferPlayerToDimension(playerMP, dimension,
                new DefaultTeleport(worldServer, x, y, z));
        playerMP.setPositionAndUpdate(x, y, z);
        if (currDimension == 1) {
            worldServer.spawnEntity(playerMP);
            worldServer.updateEntityWithOptionalForce(playerMP, false);
        }
    }

    public static void playSound(EntityPlayerMP playerMP, SoundEvent soundEvent, float volume, float pitch) {
        playerMP.playSound(soundEvent, volume, pitch);
    }

    public static void playSound(EntityPlayerMP playerMP, SoundEvent soundEvent) {
        playSound(playerMP, soundEvent, 0.5F, 1.0F);
    }

    public static ListenableFuture<?> delegateToServerThread(Runnable task) {
        return getServer().addScheduledTask(task);
    }

    public static <T> ListenableFuture<T> delegateToServerThread(Callable<T> task) {
        return getServer().callFromMainThread(task);
    }

    public static class DefaultTeleport extends Teleporter {

        private final WorldServer worldServer;

        private double xPos, yPos, zPos;

        public DefaultTeleport(WorldServer worldServer, double x, double y, double z) {
            super(worldServer);
            this.worldServer = worldServer;
            xPos = x;
            yPos = y;
            zPos = z;
        }

        @Override
        public void placeInPortal(Entity entity, float rotationYaw) {
            this.worldServer.getBlockState(new BlockPos((int) xPos, (int) yPos, (int) zPos));
            entity.setPosition(xPos, yPos, zPos);
            entity.motionX = entity.motionY = entity.motionZ = 0.0D;
        }
    }
}
