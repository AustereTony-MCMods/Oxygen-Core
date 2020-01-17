package austeretony.oxygen_core.server.privilege;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.EnumPrivilegeFileKey;
import austeretony.oxygen_core.common.privilege.Role;
import austeretony.oxygen_core.common.privilege.RoleImpl;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenPrivilegesLoadedEvent;
import net.minecraftforge.common.MinecraftForge;

public class PrivilegesLoaderServer {

    public static void loadPrivilegeData() {
        loadRoles();
        loadPlayersList();
        OxygenManagerServer.instance().getPrivilegesManager().addDefaultRoles();

        CommonReference.delegateToServerThread(()->MinecraftForge.EVENT_BUS.post(new OxygenPrivilegesLoadedEvent()));
    }

    private static void loadPlayersList() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/players.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray 
                playersArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray(),
                rolesIdsArray;
                JsonObject playerObject;
                UUID playerUUID;
                for (JsonElement playerElement : playersArray) {
                    playerObject = playerElement.getAsJsonObject();
                    playerUUID = UUID.fromString(playerObject.get(EnumPrivilegeFileKey.PLAYER_UUID.name).getAsString());
                    rolesIdsArray = playerObject.get(EnumPrivilegeFileKey.ROLES.name).getAsJsonArray();
                    for (JsonElement roleIdElement : rolesIdsArray)
                        OxygenManagerServer.instance().getPrivilegesManager().addRoleToPlayer(playerUUID, roleIdElement.getAsInt());
                    OxygenManagerServer.instance().getPrivilegesManager().setPlayerChatFormattingRole(playerUUID, playerObject.get(EnumPrivilegeFileKey.CHAT_FORMATTING_ROLE.name).getAsInt());
                }
                OxygenMain.LOGGER.info("Loaded privileged players list.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Privileged players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private static void loadRoles() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/roles.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray rolesArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement roleElement : rolesArray)
                    OxygenManagerServer.instance().getPrivilegesManager().addRole(RoleImpl.deserialize(roleElement.getAsJsonObject()));
                OxygenManagerServer.instance().getPrivilegesManager().compressRolesData();
                OxygenMain.LOGGER.info("Roles loaded.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Roles loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public static void savePlayersListAsync() {
        OxygenHelperServer.addIOTask(()->savePlayersList());
    }

    public static void savePlayersList() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/players.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            JsonArray 
            playersArray = new JsonArray(),
            rolesIdsArray;
            JsonObject playerObject;
            for (Map.Entry<UUID, PlayerRolesContainer> entry : OxygenManagerServer.instance().getPrivilegesManager().getPlayers().entrySet()) {
                playerObject = new JsonObject();
                playerObject.add(EnumPrivilegeFileKey.PLAYER_UUID.name, new JsonPrimitive(entry.getKey().toString()));
                rolesIdsArray = new JsonArray();
                for (int roleId : entry.getValue().roles)
                    rolesIdsArray.add(new JsonPrimitive(roleId));
                playerObject.add(EnumPrivilegeFileKey.ROLES.name, rolesIdsArray);
                playerObject.add(EnumPrivilegeFileKey.CHAT_FORMATTING_ROLE.name, new JsonPrimitive(entry.getValue().getChatFormattingRoleId()));
                playersArray.add(playerObject);
            }
            JsonUtils.createExternalJsonFile(folder, playersArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("Privileged players list saving failed.");
            exception.printStackTrace();
        }       
    }

    public static void savePrivilegedRolesAsync() {
        OxygenHelperServer.addIOTask(()->savePrivilegedRoles());
    }

    public static void savePrivilegedRoles() {
        String folder = OxygenHelperCommon.getConfigFolder() + "data/server/privileges/roles.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            JsonArray rolesArray = new JsonArray();
            JsonObject roleObject;
            for (Role role : OxygenManagerServer.instance().getPrivilegesManager().getRoles())
                rolesArray.add(role.serialize());
            JsonUtils.createExternalJsonFile(folder, rolesArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("Roles saving failed.");
            exception.printStackTrace();
        }       
    }
}
