package austeretony.oxygen_core.server.command;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.notification.SimpleNotification;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.config.OxygenConfig;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.scripting.ScriptWrapper;
import austeretony.oxygen_core.common.scripting.ScriptingProvider;
import austeretony.oxygen_core.common.scripting.Shell;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.CurrencyHelperServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.PrivilegesProviderServer;
import austeretony.oxygen_core.server.api.TimeHelperServer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;

public class CoreArgumentOperator implements ArgumentExecutor {

    private Shell shell;

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
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.role.add", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))); 
                            else
                                server.sendMessage(new TextComponentString(String.format("Added role <%s> to player <%s>.", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))));

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> added role <{}({})> to {}/{}.",
                                        sender.getName(),
                                        role.getName(),
                                        roleId,
                                        CommonReference.getName(targetPlayerMP),
                                        CommonReference.getPersistentUUID(targetPlayerMP));
                        } else if (args[2].equals("-remove-role")) {
                            OxygenManagerServer.instance().getPrivilegesManager().removeRoleFromPlayer(CommonReference.getPersistentUUID(targetPlayerMP), roleId);

                            if (sender instanceof EntityPlayerMP)
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.role.remove", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))); 
                            else
                                server.sendMessage(new TextComponentString(String.format("Removed role <%s> from player <%s>.", 
                                        role.getName(),
                                        CommonReference.getName(targetPlayerMP))));

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> removed role <{}({})> from {}/{}.",
                                        sender.getName(),
                                        role.getName(),
                                        roleId,
                                        CommonReference.getName(targetPlayerMP),
                                        CommonReference.getPersistentUUID(targetPlayerMP));
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
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.currency.balance", 
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
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.currency.set", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> set <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> set player {}/{} currency with index <{}> to {}.",
                                        sender.getName(),
                                        CommonReference.getName(targetPlayerMP),
                                        CommonReference.getPersistentUUID(targetPlayerMP),
                                        currencyIndex,
                                        amount);
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
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.currency.add", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> recieved <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> added {} currency with index <{}> to player {}/{}.",
                                        sender.getName(),
                                        amount,
                                        currencyIndex,
                                        CommonReference.getName(targetPlayerMP),
                                        CommonReference.getPersistentUUID(targetPlayerMP));
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
                                senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.currency.remove", 
                                        CommonReference.getName(targetPlayerMP), 
                                        String.valueOf(amount),
                                        String.valueOf(currencyIndex))); 

                            else
                                server.sendMessage(new TextComponentString(String.format("Player <%s> lost <%s> currency units with index <%s>.", 
                                        CommonReference.getName(targetPlayerMP), 
                                        amount,
                                        currencyIndex))); 

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> removed {} currency with index <{}> from player {}/{}.",
                                        sender.getName(),
                                        amount,
                                        currencyIndex,
                                        CommonReference.getName(targetPlayerMP),
                                        CommonReference.getPersistentUUID(targetPlayerMP));
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
                        senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.notification.sentTo", 
                                "ALL",
                                notification)); 
                    else
                        server.sendMessage(new TextComponentString(String.format("Notification sent to <all>: %s", 
                                notification)));

                    if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                        OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> sent notification to ALL: [{}].",
                                sender.getName(),
                                notification);
                } else {
                    targetPlayerMP = CommandBase.getPlayer(server, sender, args[2]);
                    OxygenHelperServer.addNotification(targetPlayerMP, 
                            new SimpleNotification(OxygenMain.SIMPLE_NOTIFICATION_ID, notification));

                    if (sender instanceof EntityPlayerMP)
                        senderPlayerMP.sendMessage(new TextComponentTranslation("oxygen_core.message.command.oxygenop.notification.sentTo", 
                                CommonReference.getName(targetPlayerMP),
                                notification)); 
                    else
                        server.sendMessage(new TextComponentString(String.format("Notification sent to <%s>: %s", 
                                CommonReference.getName(targetPlayerMP),
                                notification)));

                    if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                        OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> sent notification to player {}/{}: [{}].",
                                sender.getName(),
                                CommonReference.getName(targetPlayerMP),
                                CommonReference.getPersistentUUID(targetPlayerMP),
                                notification);
                }
            } else if (args[1].equals("-script")) {//TODO Script
                if (args[2].equals("-chat")) {
                    if (args.length >= 4) {
                        StringBuilder builder = new StringBuilder();
                        for (int i = 3; i < args.length; i++)
                            builder.append(args[i]).append(' ');
                        String 
                        scriptText = builder.toString(),
                        executionTimeStr;

                        if (this.shell == null)
                            this.shell = ScriptingProvider.createShell();

                        this.shell.put("sender", sender);

                        Object result = shell.evaluate(
                                scriptText, 
                                String.format("script_%d", TimeHelperServer.getCurrentMillis()), 
                                true);

                        if (result != null)
                            sender.sendMessage(new TextComponentString(String.format("Script executed. Result: %s", result)));
                        else
                            sender.sendMessage(new TextComponentString("Script executed."));

                        if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                            OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> executed script from chat. Script text: {}",
                                    sender.getName(),
                                    scriptText);
                    }
                } else if (args[2].equals("-file")) {
                    if (args.length == 4) {
                        ScriptWrapper scriptWrapper = null;
                        try {
                            scriptWrapper = ScriptWrapper.fromFile(
                                    CommonReference.getGameFolder() + "/config/oxygen/data/server/core/scripts/" + args[3], 
                                    String.format("script_%d", TimeHelperServer.getCurrentMillis()));     
                        } catch (IOException exception) {
                            OxygenMain.LOGGER.error("Filed to load script file.");
                            exception.printStackTrace();
                        }

                        if (scriptWrapper != null) {
                            if (this.shell == null)
                                this.shell = ScriptingProvider.createShell();

                            this.shell.put("sender", sender);

                            Object result = shell.evaluate(
                                    scriptWrapper.getScriptText(), 
                                    scriptWrapper.getName(), 
                                    true);

                            if (result != null)
                                sender.sendMessage(new TextComponentString(String.format("Script executed. Result: %s", result)));
                            else
                                sender.sendMessage(new TextComponentString("Script executed."));

                            if (OxygenConfig.ADVANCED_LOGGING.asBoolean())
                                OxygenMain.LOGGER.info("[Core] (Operator/Console) <{}> executed script from file. Script name: {}, text: {}",
                                        sender.getName(),
                                        args[3],
                                        scriptWrapper.getScriptText());
                        } else
                            sender.sendMessage(new TextComponentString("Failed to load script file."));
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, "-privileges", "-currency", "-notification", "-script");
        else if (args.length >= 3) {
            if (args[1].equals("-privileges"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-add-role", "-remove-role");
            else if (args[1].equals("-currency"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-balance", "-set", "-add", "-remove");
            else if (args[1].equals("-notification"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-all");
            else if (args[1].equals("-script"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-chat", "-file");
        }
        return Collections.<String>emptyList();
    }
}
