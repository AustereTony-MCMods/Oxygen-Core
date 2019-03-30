package austeretony.oxygen.common.privilege.command;

import java.util.Set;

import austeretony.oxygen.common.api.command.OxygenCommand;
import austeretony.oxygen.common.command.IArgumentExecutor;
import austeretony.oxygen.common.reference.CommonReference;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandPrivilege extends OxygenCommand {

    public static final String GROUP_NAME = "groupName";

    public CommandPrivilege(String commandName) {
        super(commandName);
    }

    @Override
    public void getArgumentExecutors(Set<IArgumentExecutor> executors) {
        executors.add(new HelpArgumentExecutor("help"));
        executors.add(new InfoArgumentExecutor("info", true));
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayer && CommonReference.isOpped((EntityPlayer) sender);
    }
}
