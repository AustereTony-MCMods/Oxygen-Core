package austeretony.oxygen_core.server.chat;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

public abstract class AbstractChatChannel implements ChatChannel {    

    @Override
    public String getUsage(ICommandSender sender) {
        return "/" + this.getName() + " <message>";
    }

    @Override
    public List<String> getAliases() {
        return Collections.<String>emptyList();
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos targetPos) {
        return Collections.<String>emptyList();
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand other) {
        return this.getName().compareTo(other.getName());
    }
}
