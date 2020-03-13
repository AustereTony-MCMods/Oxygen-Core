package austeretony.oxygen_core.common.command;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public interface ArgumentExecutor {

    String getName();

    void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException;

    default List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        return Collections.<String>emptyList();
    }
}
