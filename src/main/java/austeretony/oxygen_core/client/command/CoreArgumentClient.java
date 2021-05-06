package austeretony.oxygen_core.client.command;

import austeretony.oxygen_core.client.api.OxygenClient;
import austeretony.oxygen_core.client.api.PrivilegesClient;
import austeretony.oxygen_core.client.gui.OxygenGUIRegistry;
import austeretony.oxygen_core.client.util.MinecraftClient;
import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.command.CommandArgument;
import austeretony.oxygen_core.common.item.ItemStackWrapper;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.player.shared.PlayerSharedData;
import austeretony.oxygen_core.common.privileges.PrivilegeRegistry;
import austeretony.oxygen_core.common.util.JsonUtils;
import com.google.gson.JsonObject;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nullable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class CoreArgumentClient implements CommandArgument {

    @Override
    public String getName() {
        return "core";
    }

    @Override
    public void process(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if (args.length == 2) {
            if (args[1].equals("settings")) {
                OxygenClient.openScreenWithDelay(OxygenMain.SCREEN_ID_SETTINGS);
            } else if (args[1].equals("entity-get-id")) {
                Entity entity = MinecraftClient.getPointedEntity();
                if (entity != null) {
                    int entityId = MinecraftClient.getEntityId(entity);
                    MinecraftClient.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(String.valueOf(entityId));
                    MinecraftClient.showChatMessage("Entity id: " + entityId);
                } else {
                    MinecraftClient.showChatMessage("No pointed entity");
                }
            } else if (args[1].equals("entity-get-uuid")) {
                Entity entity = MinecraftClient.getPointedEntity();
                if (entity != null) {
                    UUID entityUUID = MinecraftClient.getEntityUUID(entity);
                    MinecraftClient.getMinecraft().ingameGUI.getChatGUI().addToSentMessages(entityUUID.toString());
                    MinecraftClient.showChatMessage("Entity UUID: " + entityUUID.toString());
                } else {
                    MinecraftClient.showChatMessage("No pointed entity");
                }
            }
        } else if (args.length == 3) {
            if (args[1].equals("open-gui")) {
                int screenId = CommandBase.parseInt(args[2], 0, Short.MAX_VALUE);
                if (OxygenGUIRegistry.getGUIRegistryMap().get(screenId) == null)
                    throw new CommandException("Oxygen screen <" + args[2] + "> not exist");

                OxygenClient.openScreenWithDelay(screenId);
            } else if (args[1].equals("balance-get")) {
                int index = CommandBase.parseInt(args[2], 0, 100);
                if (OxygenClient.getCurrencyProperties(index) == null)
                    throw new CommandException("Currency with index <" + args[2] + "> not exist");

                long balance = OxygenClient.getWatcherValue(index, 0L);
                sender.sendMessage(new TextComponentString(String.format("Your currency <%s> client balance is: %s",
                        args[2], balance)));
            } else if (args[1].equals("get-privilege-value")) {
                int id = CommandBase.parseInt(args[2], 0, 100);
                PrivilegeRegistry.Entry entry = PrivilegeRegistry.getEntry(id);
                if (entry == null)
                    throw new CommandException("Unknown privilege id: " + args[2]);

                String valueStr;
                switch (entry.getValueType()) {
                    case BOOLEAN:
                        valueStr = String.valueOf(PrivilegesClient.getBoolean(id, false));
                        break;
                    case INTEGER:
                        valueStr = String.valueOf(PrivilegesClient.getInt(id, 0));
                        break;
                    case LONG:
                        valueStr = String.valueOf(PrivilegesClient.getLong(id, 0L));
                        break;
                    case FLOAT:
                        valueStr = String.valueOf(PrivilegesClient.getFloat(id, 0F));
                        break;
                    case DOUBLE:
                        valueStr = String.valueOf(PrivilegesClient.getDouble(id, 0.0));
                        break;
                    case STRING:
                        valueStr = String.valueOf(PrivilegesClient.getString(id, "..."));
                        break;
                    default:
                        throw new CommandException("Undefined value type: " + entry.getValueType());
                }

                sender.sendMessage(new TextComponentString(String.format("Your privilege <%s/%s> value is: %s",
                        args[2], entry.getDisplayName(), valueStr)));
            } else if (args[1].equals("held-item-to-json")) {
                ItemStack heldStack = MinecraftClient.getClientPlayer().getHeldItemMainhand();
                if (heldStack != ItemStack.EMPTY) {
                    String folder = OxygenCommon.getConfigFolder() + "data/client/core/itemstack/" + args[2] + ".json";
                    Path path = Paths.get(folder);

                    JsonObject stackObject = new JsonObject();
                    stackObject.add("itemstack", ItemStackWrapper.of(heldStack).toJson());
                    try {
                        if (!Files.exists(path)) {
                            Files.createDirectories(path.getParent());
                        }

                        JsonUtils.createExternalJsonFile(folder, stackObject);
                    } catch (IOException exception) {
                        OxygenMain.logError(1, "[Core] ItemStack JSON serialization failure!", exception);
                    }
                    MinecraftClient.showChatMessage(String.format("Serialized to JSON <%s>: %s",
                            args[2], heldStack.getDisplayName()));
                } else {
                    MinecraftClient.showChatMessage("Main hand is empty");
                }
            }
        } else if (args.length == 4) {
            if (args[1].equals("get-shared-value")) {
                PlayerSharedData data = OxygenClient.getPlayerSharedData(args[2]);
                if (data == null)
                    throw new CommandException("Couldn't find player: " + args[2]);
                int id = CommandBase.parseInt(args[3], 0, Short.MAX_VALUE);

                sender.sendMessage(new TextComponentString(String.format("Shared data value of <%s> with index <%s> is: %s",
                        args[2], args[3], data.getValue(id, "unknown"))));
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args,
                                          @Nullable BlockPos targetPos) {
        if (args.length == 2)
            return CommandBase.getListOfStringsMatchingLastWord(args, "settings", "balance-get", "get-shared-value", "get-privilege-value");
        return Collections.emptyList();
    }
}
