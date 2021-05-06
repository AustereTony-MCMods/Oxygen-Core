package austeretony.oxygen_core.client.command;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.common.command.CommandArgument;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class RejectArgumentClient implements CommandArgument {

    @Override
    public String getName() {
        return "reject";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 1) {
            if (args[0].equals("reject")) {
                OxygenManagerClient.instance().getNotificationsManager().getNotificationProvider().rejectLatestRequest();
            }
        }
    }
}
