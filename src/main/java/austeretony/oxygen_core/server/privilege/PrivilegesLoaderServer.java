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
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.EnumPrivilegeFileKey;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.server.OxygenManagerServer;
import austeretony.oxygen_core.server.api.OxygenHelperServer;
import austeretony.oxygen_core.server.api.event.OxygenPrivilegesLoadedEvent;
import net.minecraftforge.common.MinecraftForge;

public class PrivilegesLoaderServer {

    public static void loadPrivilegeData() {
        loadPrivilegedGroups();
        loadPlayersList();
        OxygenManagerServer.instance().getPrivilegesManager().addDefaultGroups();

        CommonReference.delegateToServerThread(()->MinecraftForge.EVENT_BUS.post(new OxygenPrivilegesLoadedEvent()));
    }

    private static void loadPlayersList() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privileges/players.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray jsonArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                JsonObject object;
                UUID playerUUID;
                for (JsonElement element : jsonArray) {
                    object = element.getAsJsonObject();
                    playerUUID = UUID.fromString(object.get(EnumPrivilegeFileKey.PLAYER_UUID.name).getAsString());
                    OxygenManagerServer.instance().getPrivilegesManager().promotePlayer(playerUUID, object.get(EnumPrivilegeFileKey.GROUP.name).getAsString());
                }
                OxygenMain.LOGGER.info("Loaded privileged players list.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Privileged players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private static void loadPrivilegedGroups() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privileges/groups.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray groupArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement groupElement : groupArray)
                    OxygenManagerServer.instance().getPrivilegesManager().addGroup(PrivilegedGroupImpl.deserializeServer(groupElement.getAsJsonObject()), false);
                OxygenMain.LOGGER.info("Privileged groups loaded.");
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Privileged groups loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public static void savePlayersListAsync() {
        OxygenHelperServer.addIOTask(()->savePlayersList());
    }

    public static void savePlayersList() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privileges/players.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            JsonArray jsonArray = new JsonArray();
            JsonObject object;
            for (Map.Entry<UUID, String> entry : OxygenManagerServer.instance().getPrivilegesManager().getPlayers().entrySet()) {
                object = new JsonObject();
                object.add(EnumPrivilegeFileKey.PLAYER_UUID.name, new JsonPrimitive(entry.getKey().toString()));
                object.add(EnumPrivilegeFileKey.GROUP.name, new JsonPrimitive(entry.getValue()));
                jsonArray.add(object);
            }
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("Privileged players list saving failed.");
            exception.printStackTrace();
        }       
    }

    public static void savePrivilegedGroupsAsync() {
        OxygenHelperServer.addIOTask(()->savePrivilegedGroups());
    }

    public static void savePrivilegedGroups() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privileges/groups.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            JsonArray jsonArray = new JsonArray();
            JsonObject object;
            for (PrivilegedGroup group : OxygenManagerServer.instance().getPrivilegesManager().getGroups().values())
                jsonArray.add(group.serialize());
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
