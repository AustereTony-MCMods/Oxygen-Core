package austeretony.oxygen_core.common.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.util.objects.Pair;
import austeretony.oxygen_core.server.api.OxygenServer;
import com.google.gson.JsonParser;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface CommandArgument {

    String getName();

    void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    default List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                           @Nullable BlockPos targetPos) {
        return Collections.<String>emptyList();
    }

    default void checkPermission(ICommandSender sender, String permission) throws CommandException {
        //TODO Workaround for permission support (Sponge, ForgeEssentials, etc.)
        /*if (!sender.canUseCommand(0, permission)) {
            throw new CommandException("You have no permission to execute this command");
        }*/
    }

    @Nullable
    default UUID tryParseUUID(String str) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(str);
        } catch(IllegalArgumentException exception) {}
        return uuid;
    }

    @Nonnull
    default Pair<UUID, EntityPlayerMP> parsePlayer(MinecraftServer server, ICommandSender sender, String str)
            throws CommandException {
        EntityPlayerMP playerMP = null;
        UUID playerUUID = tryParseUUID(str);
        if (playerUUID == null) {
            playerMP = CommandBase.getPlayer(server, sender, str);
            playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        }
        if (OxygenServer.getPlayerSharedData(playerUUID) == null) {
            throw new CommandException("Player " + str + " doesn't exist");
        }
        return Pair.of(playerUUID, playerMP);
    }

    default ItemStackWrapper parseItemStackWrapperFromJsonString(String str) throws CommandException {
        try {
            return ItemStackWrapper.fromJson(new JsonParser().parse(str).getAsJsonObject());
        } catch (Exception exception) {
            throw new CommandException("Invalid json string");
        }
    }

    @Nonnull
    default ItemStackWrapper getHeldItemStackWrapper(EntityPlayer player) throws CommandException {
        ItemStack itemStack = player.getHeldItemMainhand();
        if (itemStack == ItemStack.EMPTY) {
            throw new CommandException("Main hand is empty");
        }
        return ItemStackWrapper.of(itemStack);
    }

    @Nonnull
    default String[] processDoubleQuotes(String[] args) throws CommandException {
        List<String> result = new ArrayList<>(1);

        StringBuilder quotedArg = null;
        for (String arg : args) {
            boolean added = false;
            if (arg.startsWith("\"")) {
                if (quotedArg != null) {
                    throw new CommandException("Invalid quotes");
                }

                quotedArg = new StringBuilder();
                if (arg.endsWith("\"")) {
                    quotedArg.append(arg, 1, arg.length() - 1);
                    result.add(quotedArg.toString());
                    quotedArg = null;
                } else {
                    quotedArg.append(arg.substring(1)).append(' ');
                }
                added = true;
            }
            if (arg.endsWith("\"") && !added) {
                if (quotedArg == null) {
                    throw new CommandException("Invalid quotes");
                }

                quotedArg.append(arg, 0, arg.length() - 1);
                added = true;
                result.add(quotedArg.toString());
                quotedArg = null;
            }

            if (!added) {
                if (quotedArg != null) {
                    quotedArg.append(arg).append(' ');
                } else {
                    result.add(arg);
                }
            }
        }
        if (quotedArg != null) {
            throw new CommandException("Invalid quotes");
        }
        return result.toArray(new String[0]);
    }
}
