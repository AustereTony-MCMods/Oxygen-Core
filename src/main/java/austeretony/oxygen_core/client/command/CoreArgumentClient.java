package austeretony.oxygen_core.client.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import austeretony.oxygen_core.client.OxygenGUIManager;
import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.client.api.PrivilegesProviderClient;
import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.command.ArgumentExecutor;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry;
import austeretony.oxygen_core.common.privilege.PrivilegeRegistry.PrivilegeRegistryEntry;
import austeretony.oxygen_core.common.util.JsonUtils;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

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
                    OxygenHelperClient.scheduleTask(()->ClientReference.delegateToClientThread(OxygenGUIManager::openPrivilegesMenu), 100L, TimeUnit.MILLISECONDS);
                else if (args.length == 3) {
                    if (args[2].equals("-management")) 
                        OxygenHelperClient.scheduleTask(()->ClientReference.delegateToClientThread(OxygenGUIManager::openPrivilegesManagementMenu), 100L, TimeUnit.MILLISECONDS);
                } else if (args.length == 4) {
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
            } else if (args[1].equals("-notifications"))//TODO Notification
                OxygenHelperClient.scheduleTask(()->ClientReference.delegateToClientThread(OxygenGUIManager::openNotificationsMenu), 100L, TimeUnit.MILLISECONDS);
            else if (args[1].equals("-settings")) {//TODO Settings
                if (args.length == 2)
                    OxygenHelperClient.scheduleTask(()->ClientReference.delegateToClientThread(OxygenGUIManager::openSettingsMenu), 100L, TimeUnit.MILLISECONDS);
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
            } else if (args[1].equals("-entity")) {//TODO Entity
                if (args.length == 3) {
                    if (args[2].equals("-get-id")) {
                        if (ClientReference.getPointedEntity() != null) {
                            int entityId = ClientReference.getEntityId(ClientReference.getPointedEntity());
                            ClientReference.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(String.valueOf(entityId));
                            ClientReference.showChatMessage(String.format("Entity id: %d", entityId));
                        }
                    } else if (args[2].equals("-get-uuid")) {
                        if (ClientReference.getPointedEntity() != null) {
                            UUID entityUUID = ClientReference.getPersistentUUID(ClientReference.getPointedEntity());
                            ClientReference.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(String.valueOf(entityUUID));
                            ClientReference.showChatMessage(String.format("Entity UUID: %s", entityUUID));
                        }
                    }
                }
            } else if (args[1].equals("-item")) {//TODO Item
                if (args.length == 4) {
                    if (args[2].equals("-to-json")) {
                        if (ClientReference.getClientPlayer().getHeldItemMainhand() != ItemStack.EMPTY) {
                            String folder = OxygenHelperCommon.getConfigFolder() + "data/client/core/itemstack/" + args[3] + ".json";
                            Path path = Paths.get(folder);
                            JsonObject stackObject = new JsonObject();
                            stackObject.add("itemstack", ItemStackWrapper.of(ClientReference.getClientPlayer().getHeldItemMainhand()).toJson());
                            try {
                                if (!Files.exists(path))
                                    Files.createDirectories(path.getParent());   
                                JsonUtils.createExternalJsonFile(folder, stackObject);
                            } catch (IOException exception) {
                                OxygenMain.LOGGER.error("[Core] ItemStack JSON serialization failure!", exception);
                            }
                            ClientReference.showChatMessage("oxygen_core.command.oxygenc.itemStackSerialized", 
                                    ClientReference.getClientPlayer().getHeldItemMainhand().getDisplayName());
                        } else
                            throw new WrongUsageException("Main hand is empty!"); 
                    }
                }
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, "-privileges", "-currency", "-notifications", "-settings", "-request", "-entity", "-item");
        else if (args.length >= 3) {
            if (args[1].equals("-privileges"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-management", "-get-value");
            else if (args[1].equals("-currency"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-balance");
            else if (args[1].equals("-settings"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-reload-settings", "-reload-properties");
            else if (args[1].equals("-request"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-accept", "-reject");
            else if (args[1].equals("-entity"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-get-id");
            else if (args[1].equals("-item"))
                return CommandBase.getListOfStringsMatchingLastWord(args, "-to-json");
        }
        return Collections.<String>emptyList();
    }
}
