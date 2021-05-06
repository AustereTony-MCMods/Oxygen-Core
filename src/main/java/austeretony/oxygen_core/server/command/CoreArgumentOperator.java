package austeretony.oxygen_core.server.command;

import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.notification.NotificationImpl;
import austeretony.oxygen_core.common.notification.NotificationMode;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.privileges.PrivilegeRegistry;
import austeretony.oxygen_core.common.util.CallingThread;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import austeretony.oxygen_core.common.command.CommandArgument;
import austeretony.oxygen_core.common.player.ActivityStatus;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenServer;
import austeretony.oxygen_core.server.api.PrivilegesServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class CoreArgumentOperator implements CommandArgument {

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 3) {
            if (args[1].equals("status-get")) {
                EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                ActivityStatus status = OxygenManagerServer.instance().getPlayersDataManager()
                        .getPlayerStatus(MinecraftCommon.getEntityUUID(playerMP));

                if (status != null) {
                    sender.sendMessage(new TextComponentString(String.format("Activity status of <%s> is: %s",
                            MinecraftCommon.getEntityName(playerMP), status.toString().toLowerCase())));
                }
            }
        } else if (args.length >= 4) {
            if (args.length == 4) {
                if (args[1].equals("status-set")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    ActivityStatus status;
                    try {
                        status = ActivityStatus.valueOf(args[3].toUpperCase());
                    } catch (IllegalArgumentException exception) {
                        throw new CommandException("Unknown status: " + args[3]);
                    }

                    Future<Boolean> future = OxygenServer.addTask(
                            () -> OxygenManagerServer.instance().getPlayersDataManager().setPlayerStatus(playerMP, status));
                    try {
                        Boolean result = future.get();
                        if (result != null && result) {
                            sender.sendMessage(new TextComponentString(String.format("Activity status of <%s> set to: %s",
                                    MinecraftCommon.getEntityName(playerMP), args[3])));
                            OxygenMain.logInfo(1, "[Core] {} changed activity status of {}/{} to: {}.",
                                    sender.getName(), MinecraftCommon.getEntityName(playerMP),
                                    MinecraftCommon.getEntityUUID(playerMP), args[3]);
                        } else {
                            sender.sendMessage(new TextComponentString(String.format("Failed to change activity status of <%s>",
                                    MinecraftCommon.getEntityName(playerMP))));
                        }
                    } catch (InterruptedException | ExecutionException exception) {
                        exception.printStackTrace();
                    }
                } else if (args[1].equals("balance-get")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int index = CommandBase.parseInt(args[3], 0, 100);
                    if (OxygenServer.getCurrencyProvider(index) == null) {
                        throw new CommandException("Currency with index <" + args[3] + "> not exist");
                    }

                    OxygenServer.queryBalance(MinecraftCommon.getEntityUUID(playerMP), MinecraftCommon.getEntityName(playerMP), index,
                            balance -> sender.sendMessage(new TextComponentString(String.format("Currency <%s> balance of <%s> is: %s",
                                    args[3], MinecraftCommon.getEntityName(playerMP), balance))),
                            CallingThread.MINECRAFT);
                } else if (args[1].equals("shared-value-get")) {
                    PlayerSharedData data = OxygenServer.getPlayerSharedData(args[2]);
                    if (data == null) {
                        throw new CommandException("Couldn't find player " + args[2]);
                    }
                    int id = CommandBase.parseInt(args[3], 0, Short.MAX_VALUE);

                    sender.sendMessage(new TextComponentString(String.format("Shared data value of <%s>  with index <%s> is: %s",
                            args[2], args[3], data.getValue(id, "unknown"))));
                } else if (args[1].equals("sync-shared-data")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    boolean observedData = CommandBase.parseBoolean(args[3]);

                    OxygenServer.addTask(() -> {
                        if (observedData) {
                            OxygenServer.syncObservedPlayersSharedData(playerMP, -1);
                        } else {
                            OxygenServer.syncSharedData(playerMP, -1);
                        }
                    });
                    sender.sendMessage(new TextComponentString(String.format("Shared data synchronized with <%s>",
                            MinecraftCommon.getEntityName(playerMP))));
                } else if (args[1].equals("privilege-value-get")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int id = CommandBase.parseInt(args[3], 0, 100);
                    PrivilegeRegistry.Entry privilegeEntry = PrivilegeRegistry.getEntry(id);
                    if (privilegeEntry == null) {
                        throw new CommandException("Unknown privilege id: " + args[3]);
                    }

                    UUID playerUUID = MinecraftCommon.getEntityUUID(playerMP);
                    String valueStr;
                    switch (privilegeEntry.getValueType()) {
                        case BOOLEAN:
                            valueStr = String.valueOf(PrivilegesServer.getBoolean(playerUUID, id, false));
                            break;
                        case INTEGER:
                            valueStr = String.valueOf(PrivilegesServer.getInt(playerUUID, id, 0));
                            break;
                        case LONG:
                            valueStr = String.valueOf(PrivilegesServer.getLong(playerUUID, id, 0L));
                            break;
                        case FLOAT:
                            valueStr = String.valueOf(PrivilegesServer.getFloat(playerUUID, id, 0F));
                            break;
                        case DOUBLE:
                            valueStr = String.valueOf(PrivilegesServer.getDouble(playerUUID, id, 0.0));
                            break;
                        case STRING:
                            valueStr = String.valueOf(PrivilegesServer.getString(playerUUID, id, ""));
                            break;
                        default:
                            throw new CommandException("Undefined value type: " + args[3]);
                    }

                    sender.sendMessage(new TextComponentString(String.format("Player <%s> privilege <%s/%s> value is: %s",
                            MinecraftCommon.getEntityName(playerMP), args[2], privilegeEntry.getDisplayName(), valueStr)));
                } else if (args[1].equals("open-gui")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int screenId = CommandBase.parseInt(args[3], 0, Short.MAX_VALUE);
                    OxygenServer.openScreen(playerMP, screenId);
                }
            }

            if (args.length == 5) {
                if (args[1].equals("balance-set")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int index = CommandBase.parseInt(args[3], 0, 100);
                    if (OxygenServer.getCurrencyProvider(index) == null) {
                        throw new CommandException("Currency with index " + args[3] + " not exist");
                    }
                    long value = CommandBase.parseLong(args[4], Long.MIN_VALUE, Long.MAX_VALUE);

                    OxygenManagerServer.instance().getCurrencyManager().setBalance(
                            MinecraftCommon.getEntityUUID(playerMP),
                            MinecraftCommon.getEntityName(playerMP),
                            index,
                            value,
                            CallingThread.MINECRAFT,
                            balance -> {
                                if (balance != null) {
                                    sender.sendMessage(new TextComponentString(String.format("Set currency <%s> balance of <%s> to: %s",
                                            args[3], MinecraftCommon.getEntityName(playerMP), value)));

                                    OxygenMain.logInfo(1, "[Core] {} set <{}> currency balance of <{}/{}> to {}.",
                                            sender.getName(), args[3], MinecraftCommon.getEntityName(playerMP),
                                            MinecraftCommon.getEntityUUID(playerMP), args[4]);
                                } else {
                                    sender.sendMessage(new TextComponentString("Failed to update currency balance"));
                                }
                            });
                } else if (args[1].equals("balance-add")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int index = CommandBase.parseInt(args[3], 0, 100);
                    if (OxygenServer.getCurrencyProvider(index) == null) {
                        throw new CommandException("Currency with index " + args[3] + " not exist");
                    }
                    long value = CommandBase.parseLong(args[4], 1L, Long.MAX_VALUE);

                    OxygenManagerServer.instance().getCurrencyManager().incrementBalance(
                            MinecraftCommon.getEntityUUID(playerMP),
                            MinecraftCommon.getEntityName(playerMP),
                            index,
                            value,
                            CallingThread.MINECRAFT,
                            balance -> {
                                if (balance != null) {
                                    sender.sendMessage(new TextComponentString(String.format("Added currency <%s> to <%s>: %s",
                                            args[3], MinecraftCommon.getEntityName(playerMP), value)));

                                    OxygenMain.logInfo(1, "[Core] {} added {} to <{}> currency balance of <{}/{}>.",
                                            sender.getName(), args[4], args[3], MinecraftCommon.getEntityName(playerMP),
                                            MinecraftCommon.getEntityUUID(playerMP));
                                } else {
                                    sender.sendMessage(new TextComponentString("Failed to update currency balance"));
                                }
                            });
                } else if (args[1].equals("balance-remove")) {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, args[2]);
                    int index = CommandBase.parseInt(args[3], 0, 100);
                    if (OxygenServer.getCurrencyProvider(index) == null) {
                        throw new CommandException("Currency with index " + args[3] + " not exist");
                    }
                    long value = CommandBase.parseLong(args[4], 1L, Long.MAX_VALUE);

                    OxygenManagerServer.instance().getCurrencyManager().decrementBalance(
                            MinecraftCommon.getEntityUUID(playerMP),
                            MinecraftCommon.getEntityName(playerMP),
                            index,
                            value,
                            CallingThread.MINECRAFT,
                            balance -> {
                                if (balance != null) {
                                    sender.sendMessage(new TextComponentString(String.format("Removed currency <%s> of <%s>: %s",
                                            args[3], MinecraftCommon.getEntityName(playerMP), value)));
                                    OxygenMain.logInfo(1, "[Core] {} removed {} from <{}> currency balance of <{}/{}>.",
                                            sender.getName(), args[4], args[3], MinecraftCommon.getEntityName(playerMP),
                                            MinecraftCommon.getEntityUUID(playerMP));
                                } else {
                                    sender.sendMessage(new TextComponentString("Failed to update currency balance"));
                                }
                            });
                }
            }

            if (args.length >= 5 && args[1].equals("send-notification")) {
                String target = args[2];
                int notificationId = CommandBase.parseInt(args[3], 0, 9);
                NotificationMode mode = NotificationMode.valueOf(args[4].toUpperCase(Locale.ROOT));

                StringBuilder builder = new StringBuilder();
                for (int i = 5; i < args.length; i++) {
                    builder.append(args[i]).append(' ');
                }
                String notificationText = builder.toString().trim();

                if (target.equals("@a")) {
                    for (PlayerSharedData data : OxygenServer.getOnlinePlayersSharedData()) {
                        EntityPlayerMP playerMP = MinecraftCommon.getPlayerByUUID(data.getPlayerUUID());
                        if (playerMP != null) {
                            NotificationImpl
                                    .builder(notificationId, notificationText)
                                    .withMode(mode)
                                    .send(playerMP);
                        }
                    }

                    sender.sendMessage(new TextComponentString("Successfully sent notification to all online players: " +
                            notificationText));
                    OxygenMain.logInfo(1, "[Core] {} sent notification to all players online: {}.",
                            sender.getName(), notificationText);
                } else {
                    EntityPlayerMP playerMP = CommandBase.getPlayer(server, sender, target);
                    NotificationImpl
                            .builder(notificationId, notificationText)
                            .withMode(mode)
                            .send(playerMP);

                    sender.sendMessage(new TextComponentString(String.format("Successfully sent notification to <%s>: %s",
                            MinecraftCommon.getEntityName(playerMP), notificationText)));
                    OxygenMain.logInfo(1, "[Core] {} sent notification to {}/{}: {}.",
                            sender.getName(), MinecraftCommon.getEntityName(playerMP), MinecraftCommon.getEntityUUID(playerMP),
                            notificationText);
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos targetPos) {
        if (args.length == 2) {
            return CommandBase.getListOfStringsMatchingLastWord(args, "status-get", "status-set",
                    "shared-value-get", "sync-shared-data", "privilege-value-get", "balance-get", "balance-set",
                    "balance-add", "balance-removed", "send-notification", "open-gui");
        }
        return Collections.emptyList();
    }
}
