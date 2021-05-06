package austeretony.oxygen_core.server.command;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.network.packets.client.CPOpenOxygenGUI;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.command.CommandArgument;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CoreArgumentServer implements CommandArgument {

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (!(sender instanceof EntityPlayerMP))
            throw new WrongUsageException("Command available only for players");

        EntityPlayerMP playerMP = (EntityPlayerMP) sender;
        UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
        String username = MinecraftCommon.getEntityName(playerMP);
        if (args.length == 2) {
            if (args[1].equals("status-get")) {
                ActivityStatus status = OxygenManagerServer.instance().getPlayersDataManager().getPlayerStatus(playerUUID);

                if (status != null)
                    playerMP.sendMessage(new TextComponentString("Your activity status is: "
                            + status.toString().toLowerCase()));
            }
        } else if (args.length == 3) {
            if (args[1].equals("status-set")) {
                if (OxygenServer.isNetworkRequestAvailable(OxygenMain.NET_REQUEST_ACTIVITY_STATUS_CHANGE,
                        MinecraftCommon.getEntityUUID(playerMP))) {
                    ActivityStatus status;
                    try {
                        status = ActivityStatus.valueOf(args[2].toUpperCase());
                    } catch (IllegalArgumentException exception) {
                        throw new CommandException("Unknown status: " + args[3]);
                    }

                    OxygenServer.addTask(() -> {
                        OxygenManagerServer.instance().getPlayersDataManager().setPlayerStatus(playerMP, status);
                    });
                    playerMP.sendMessage(new TextComponentString("Your activity status successfully changed to: "
                            + args[2]));
                }
            } else if (args[1].equals("balance-get")) {
                int index = CommandBase.parseInt(args[2], 0, 100);
                if (OxygenServer.getCurrencyProvider(index) == null)
                    throw new CommandException("Currency with index " + args[2] + " not exist");

                OxygenServer.queryBalance(playerUUID, username, index,
                        balance -> playerMP.sendMessage(new TextComponentString(String.format("Your currency <%s> balance is: %s",
                                args[2], balance))),
                        CallingThread.MINECRAFT);
            } else if (args[1].equals("open-gui")) {
                int screenId = CommandBase.parseInt(args[2], 0, Short.MAX_VALUE);

                OxygenMain.network().sendTo(new CPOpenOxygenGUI(screenId), playerMP);
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos targetPos) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, "status-get", "status-set", "balance-get");
        return Collections.emptyList();
    }
}
