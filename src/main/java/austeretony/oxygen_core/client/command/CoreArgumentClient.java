package austeretony.oxygen_core.client.command;

import java.util.concurrent.TimeUnit;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.PrivilegesProviderClient;
import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.client.gui.privileges.PrivilegesScreen;
import austeretony.oxygen_core.client.gui.settings.SettingsScreen;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CoreArgumentClient implements ArgumentExecutor {

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length >= 2) {
            if (args[1].equals("-privileges")) {//TODO Privileges
                if (args.length == 2)
                    OxygenHelperClient.scheduleTask(()->this.openPrivilegesMenu(), 100L, TimeUnit.MILLISECONDS);    
                else if (args.length == 4) {
                    if (args[2].equals("-get-value"))  {
                        int privilegeId = CommandBase.parseInt(args[3], 0, Short.MAX_VALUE);
                        PrivilegeRegistryEntry entry = PrivilegeRegistry.getRegistryEntry(privilegeId);
                        if (entry != null) {
                            switch (entry.type) {
                            case BOOLEAN:
                                ClientReference.showChatMessage("oxygen_core.command.oxygenc.privileges.getValue", 
                                        entry.name, 
                                        entry.id,
                                        PrivilegesProviderClient.getAsBoolean(privilegeId, false));
                                break;
                            case INT:
                                ClientReference.showChatMessage("oxygen_core.command.oxygenc.privileges.getValue", 
                                        entry.name, 
                                        entry.id,
                                        PrivilegesProviderClient.getAsInt(privilegeId, - 1));
                                break;
                            case LONG:
                                ClientReference.showChatMessage("oxygen_core.command.oxygenc.privileges.getValue", 
                                        entry.name, 
                                        entry.id,
                                        PrivilegesProviderClient.getAsLong(privilegeId, - 1L));
                                break;
                            case FLOAT:
                                ClientReference.showChatMessage("oxygen_core.command.oxygenc.privileges.getValue", 
                                        entry.name, 
                                        entry.id,
                                        PrivilegesProviderClient.getAsFloat(privilegeId, - 1.0F));
                                break;
                            case STRING:
                                ClientReference.showChatMessage("oxygen_core.command.oxygenc.privileges.getValue", 
                                        entry.name, 
                                        entry.id,
                                        PrivilegesProviderClient.getAsString(privilegeId, "NONE"));
                                break;
                            default:
                                break;                       
                            }
                        }
                    }
                }
            } else if (args[1].equals("-currency")) {//TODO Currency
                if (args.length == 4) {
                    if (args[2].equals("-balance")) {
                        int currencyIndex = CommandBase.parseInt(args[3], 0, 127);

                        if (OxygenManagerClient.instance().getCurrencyManager().getProperties(currencyIndex) == null)
                            throw new WrongUsageException("Invalid currency index: %s", currencyIndex);

                        ClientReference.showChatMessage("oxygen_core.command.oxygenc.currency.balance", currencyIndex, WatcherHelperClient.getLong(currencyIndex));
                    }
                }
            } else if (args[1].equals("-notifications")) {//TODO Notification
                OxygenHelperClient.scheduleTask(()->this.openNotificationsMenu(), 100L, TimeUnit.MILLISECONDS);
            } else if (args[1].equals("-settings")) {//TODO Settings
                if (args.length == 2)
                    OxygenHelperClient.scheduleTask(()->this.openSettingsMenu(), 100L, TimeUnit.MILLISECONDS);
                else if (args[2].equals("-reload-settings")) {
                    OxygenManagerClient.instance().getClientSettingManager().loadSettings();
                    ClientReference.showChatMessage("oxygen_core.command.oxygenc.reloadSettings");
                } else if (args[2].equals("-reload-properties")) {
                    OxygenManagerClient.instance().getCurrencyManager().loadProperties();
                    ClientReference.showChatMessage("oxygen_core.command.oxygenc.reloadProperties");
                }
            } else if (args[1].equals("-request")) {//TODO Request
                if (args.length == 3) {
                    if (args[2].equals("-accept"))
                        OxygenManagerClient.instance().getNotificationsManager().acceptRequestSynced();
                    else if (args[2].equals("-reject"))
                        OxygenManagerClient.instance().getNotificationsManager().rejectRequestSynced();
                }
            }
        }
    }

    private void openPrivilegesMenu() {
        ClientReference.delegateToClientThread(()->ClientReference.displayGuiScreen(new PrivilegesScreen()));
    }

    private void openNotificationsMenu() {
        ClientReference.delegateToClientThread(()->OxygenManagerClient.instance().getNotificationsManager().openNotificationsMenu());
    }

    private void openSettingsMenu() {
        ClientReference.delegateToClientThread(()->ClientReference.displayGuiScreen(new SettingsScreen()));
    }
}
