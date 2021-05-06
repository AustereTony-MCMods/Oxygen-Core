package austeretony.oxygen_core.server.command;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

import austeretony.oxygen_core.common.api.OxygenCommon;
import austeretony.oxygen_core.common.command.AbstractOxygenCommand;
import austeretony.oxygen_core.common.command.CommandArgument;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.common.util.MinecraftCommon;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandOxygenOperator extends AbstractOxygenCommand {

    private static final Set<CommandArgument> ARGUMENTS = new HashSet<>();

    private static int permissionLevel = 2;
    private static boolean mcOperatorAccess = true;
    private static final Set<String> USERS = new HashSet<>();

    public CommandOxygenOperator(String commandName) {
        super(commandName);
        loadSettings();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return permissionLevel;
    }

    @Override
    public void getArgumentExecutors(Set<CommandArgument> executors) {
        executors.addAll(ARGUMENTS);
    }

    @Override
    public boolean valid(MinecraftServer server, ICommandSender sender) {
        if (sender instanceof EntityPlayer) {
            return isPlayerOperator((EntityPlayer) sender);
        }
        return sender.canUseCommand(getRequiredPermissionLevel(), getName());
    }

    public static boolean isPlayerOperator(EntityPlayer player) {
        if (mcOperatorAccess && MinecraftCommon.isPlayerOperator(player)) {
            return true;
        }
        if (USERS.contains(MinecraftCommon.getEntityName(player))
                || USERS.contains(MinecraftCommon.getEntityUUID(player).toString())) {
            return true;
        }
        return false;
    }

    public static void registerArgument(CommandArgument executor) {
        ARGUMENTS.add(executor);
    }

    private static void loadSettings() {
        String folder = OxygenCommon.getConfigFolder() + "/data/server/core/oxygenop_settings.json";
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try {
                JsonObject settingsObject = JsonUtils.getExternalJsonData(folder).getAsJsonObject();

                mcOperatorAccess = settingsObject.get("enable_mc_op_access").getAsBoolean();
                permissionLevel = settingsObject.get("command_permission_level").getAsInt();

                settingsObject.getAsJsonArray("users")
                        .forEach(e -> USERS.add(e.getAsString()));

                OxygenMain.logInfo(1, "[Core] Loaded 'oxygenop' command settings.");
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to load 'oxygenop' command settings: {}", folder);
                exception.printStackTrace();
            }
        } else {
            try {
                if (!Files.exists(path)) {
                    Files.createDirectories(path.getParent());
                }

                JsonObject settingsObject = new JsonObject();

                settingsObject.addProperty("enable_mc_op_access", mcOperatorAccess);
                settingsObject.addProperty("command_permission_level", permissionLevel);
                settingsObject.add("users", new JsonArray());

                JsonUtils.createExternalJsonFile(folder, settingsObject);
            } catch (IOException exception) {
                OxygenMain.logError(1, "[Core] Failed to create default 'oxygenop' command setting file: {}",
                        folder);
                exception.printStackTrace();
            }
        }
    }
}
