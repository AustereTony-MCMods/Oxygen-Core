package austeretony.oxygen.common.privilege.io;

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

import austeretony.oxygen.common.OxygenManagerServer;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.util.JsonUtils;
import austeretony.oxygen.common.util.OxygenUtils;

public class PrivilegeLoaderServer {

    private final OxygenManagerServer manager;

    public PrivilegeLoaderServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void loadPrivilegeDataDelegated() {
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadPrivilegedGroups();
                loadPlayerList();
                manager.getPrivilegeManager().addDefaultGroups();//It should be somewhere...
            }           
        });
    }

    private void loadPlayerList() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/players.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray jsonArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                JsonObject object;
                UUID playerUUID;
                for (JsonElement element : jsonArray) {
                    object = element.getAsJsonObject();
                    playerUUID = new UUID(
                            object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PLAYER_UUID_MSB)).getAsLong(),
                            object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PLAYER_UUID_LSB)).getAsLong());
                    manager.getPrivilegeManager().promotePlayer(playerUUID, object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.GROUP)).getAsString());
                }
                OxygenMain.PRIVILEGE_LOGGER.info("Loaded player list.");
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private void loadPrivilegedGroups() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/groups.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray groupArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement groupElement : groupArray)
                    manager.getPrivilegeManager().addGroup(PrivilegedGroup.deserializeServer(groupElement.getAsJsonObject()), false);
                OxygenMain.PRIVILEGE_LOGGER.info("Privileged groups loaded.");
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public void savePlayerListDelegated() {
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePlayerList();
            }           
        });
    }

    public void savePlayerList() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/players.json";
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
            for (Map.Entry<UUID, String> entry : manager.getPrivilegeManager().getPlayers().entrySet()) {
                object = new JsonObject();
                object.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PLAYER_UUID_MSB), new JsonPrimitive(entry.getKey().getMostSignificantBits()));
                object.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PLAYER_UUID_LSB), new JsonPrimitive(entry.getKey().getLeastSignificantBits()));
                object.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.GROUP), new JsonPrimitive(entry.getValue()));
                jsonArray.add(object);
            }
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.PRIVILEGE_LOGGER.error("Players list saving failed.");
            exception.printStackTrace();
        }       
    }

    public void savePrivilegedGroupsDelegated() {
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePrivilegedGroups();
            }           
        });
    }

    public void savePrivilegedGroups() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/groups.json";
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
            for (IPrivilegedGroup group : manager.getPrivilegeManager().getGroups().values())
                jsonArray.add(group.serialize());
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
