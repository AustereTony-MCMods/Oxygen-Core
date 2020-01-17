package austeretony.oxygen_core.server.command;

import java.util.UUID;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.notification.SimpleNotification;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegesProviderServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CoreArgumentOperator implements ArgumentExecutor {

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 3) {
            EntityPlayerMP senderPlayerMP = null, targetPlayerMP;
            if (sender instanceof EntityPlayerMP)
                senderPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
            if (args[1].equals("-privileges")) {//TODO Privileges
                if (args.length == 5) {
                    targetPlayerMP = CommandBase.getPlayer(server, sender, args[3]);
                    if (sender instanceof EntityPlayerMP)
                        senderPlayerMP = CommandBase.getCommandSenderAsPlayer(sender);
                    int roleId = CommandBase.parseInt(args[4], 0, 127);
                    Role role = PrivilegesProviderServer.getRole(roleId);
                    if (role != null) {
                        if (args[2].equals("-add-role")) {
                            OxygenManagerServer.instance().getPrivilegesManager().addRoleToPlayer(CommonReference.getPersistentUUID(targetPlayerMP), roleId);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.role.add", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))); 
                            else
                                server.sendMessage(new TextComponentString(String.format("Added role <%s> to player <%s>.", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))));
                        } else if (args[2].equals("-remove-role")) {
                            OxygenManagerServer.instance().getPrivilegesManager().removeRoleFromPlayer(CommonReference.getPersistentUUID(targetPlayerMP), roleId);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.role.remove", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))); 
                            else
                                server.sendMessage(new TextComponentString(String.format("Removed role <%s> from player <%s>.", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))));
                        }
                    }
                }
            } else if (args[1].equals("-currency")) {//TODO Currency
                if (args.length >= 5) {
                    UUID playerUUID;
                    int currencyIndex = OxygenMain.COMMON_CURRENCY_INDEX;
                    long amount = 0L;
                    if (args[2].equals("-balance")) {
                        if (args.length == 5) {
                            targetPlayerMP = CommandBase.getPlayer(server, sender, args[3]);
                            currencyIndex = CommandBase.parseInt(args[4], 0, 127);

                            if (CurrencyHelperServer.getCurrencyProvider(currencyIndex) == null)
                                throw new WrongUsageException("Invalid currency index: %s", currencyIndex);

                            long balance = CurrencyHelperServer.getCurrency(CommonReference.getPersistentUUID(targetPlayerMP), currencyIndex);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.currency.balance", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(currencyIndex),
                                        String.valueOf(balance))); 
                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> currency <%s> amount is <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        currencyIndex,
                                        balance)));
                        }
                    } else if (args[2].equals("-set")) {
                        if (args.length == 6) {
                            targetPlayerMP = CommandBase.getPlayer(server, sender, args[3]);
                            currencyIndex = CommandBase.parseInt(args[4], 0, 127);
                            amount = CommandBase.parseLong(args[5], 0, Long.MAX_VALUE);
                            playerUUID = CommonReference.getPersistentUUID(targetPlayerMP);

                            if (CurrencyHelperServer.getCurrencyProvider(currencyIndex) == null)
                                throw new WrongUsageException("Invalid currency index: %s", currencyIndex);

                            CurrencyHelperServer.setCurrency(playerUUID, amount, currencyIndex);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.currency.set", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> set <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 
                        }
                    } else if (args[2].equals("-add")) {
                        if (args.length == 6) {
                            targetPlayerMP = CommandBase.getPlayer(server, sender, args[3]);
                            currencyIndex = CommandBase.parseInt(args[4], 0, 127);
                            amount = CommandBase.parseLong(args[5], 0, Long.MAX_VALUE);
                            playerUUID = CommonReference.getPersistentUUID(targetPlayerMP);

                            if (CurrencyHelperServer.getCurrencyProvider(currencyIndex) == null)
                                throw new WrongUsageException("Invalid currency index: %s", currencyIndex);

                            CurrencyHelperServer.addCurrency(playerUUID, amount, currencyIndex);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.currency.add", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> recieved <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 
                        }
                    } else if (args[2].equals("-remove")) {
                        if (args.length == 6) {
                            targetPlayerMP = CommandBase.getPlayer(server, sender, args[3]);
                            currencyIndex = CommandBase.parseInt(args[4], 0, 127);
                            amount = CommandBase.parseLong(args[5], 0, Long.MAX_VALUE);
                            playerUUID = CommonReference.getPersistentUUID(targetPlayerMP);

                            if (CurrencyHelperServer.getCurrencyProvider(currencyIndex) == null)
                                throw new WrongUsageException("Invalid currency index: %s", currencyIndex);

                            CurrencyHelperServer.removeCurrency(playerUUID, amount, currencyIndex);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.currency.remove", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> lost <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 
                        }
                    }
                }
            } else if (args[1].equals("-notification")) {//TODO Notification
                StringBuilder builder = new StringBuilder();
                for (int i = 3; i < args.length; i++)
                    builder.append(args[i]).append(' ');
                String notification = builder.toString();

                if (args[2].equals("-all")) {  
                    for (UUID playerUUID : OxygenHelperServer.getOnlinePlayersUUIDs()) {
                        targetPlayerMP = CommonReference.playerByUUID(playerUUID);
                        if (targetPlayerMP != null)
                            OxygenHelperServer.addNotification(targetPlayerMP, 
                                    new SimpleNotification(OxygenMain.SIMPLE_NOTIFICATION_ID, notification));
                    }

                    if (sender instanceof EntityPlayerMP)
                        senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.notification.sentTo", 
                                "ALL",
                                notification)); 
                    else
                        server.sendMessage(new TextComponentString(String.format("Notification sent to <all>: %s", 
                                notification)));
                } else {
                    targetPlayerMP = CommandBase.getPlayer(server, sender, args[2]);
                    OxygenHelperServer.addNotification(targetPlayerMP, 
                            new SimpleNotification(OxygenMain.SIMPLE_NOTIFICATION_ID, notification));

                    if (sender instanceof EntityPlayerMP)
                        senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygens.notification.sentTo", 
                                CommonReference.getName(targetPlayerMP),
                                notification)); 
                    else
                        server.sendMessage(new TextComponentString(String.format("Notification sent to <%s>: %s", 
                                CommonReference.getName(targetPlayerMP),
                                notification)));
                }
            }
        }
    }
}
