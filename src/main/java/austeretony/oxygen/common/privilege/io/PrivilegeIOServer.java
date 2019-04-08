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

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.io.OxygenIOServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerServer;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.util.JsonUtils;
import austeretony.oxygen.common.util.OxygenUtils;

public class PrivilegeIOServer {


    private PrivilegeIOServer() {
        this.loadPrivilegeDataDelegated();
    }

    public static PrivilegeIOServer create() {
        return new PrivilegeIOServer();
    }

    public static PrivilegeIOServer instance() {
        return OxygenManagerServer.instance().getPrivilegeManager().getIO();
    }

    public void loadPrivilegeDataDelegated() {
        OxygenHelperServer.addIOTaskServer(new IOxygenTask() {

            @Override
            public void execute() {
                loadPrivilegedGroups();
                loadPlayerList();
                PrivilegeManagerServer.instance().addDefaultGroups();//It should be somewhere...
            }           
        });
    }

    private void loadPlayerList() {
        String folder = OxygenIOServer.getDataFolder() + "/server/privilege/players.json";
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
                    PrivilegeManagerServer.instance().promotePlayer(playerUUID, object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.GROUP)).getAsString());
                }
                OxygenMain.PRIVILEGE_LOGGER.info("Loaded player list.");
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private void loadPrivilegedGroups() {
        String folder = OxygenIOServer.getDataFolder() + "/server/privilege/groups.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray groupArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement groupElement : groupArray)
                    PrivilegeManagerServer.instance().addGroup(PrivilegedGroup.deserializeServer(groupElement.getAsJsonObject()), false);
                OxygenMain.PRIVILEGE_LOGGER.info("Privileged groups loaded.");
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public void savePlayerListDelegated() {
        OxygenHelperServer.addIOTaskServer(new IOxygenTask() {

            @Override
            public void execute() {
                savePlayerList();
            }           
        });
    }

    public void savePlayerList() {
        String folder = OxygenIOServer.getDataFolder() + "/server/privilege/players.json";
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
            for (Map.Entry<UUID, String> entry : PrivilegeManagerServer.instance().getPlayers().entrySet()) {
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
        OxygenHelperServer.addIOTaskServer(new IOxygenTask() {

            @Override
            public void execute() {
                savePrivilegedGroups();
            }           
        });
    }

    public void savePrivilegedGroups() {
        String folder = OxygenIOServer.getDataFolder() + "/server/privilege/groups.json";
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
            for (IPrivilegedGroup group : PrivilegeManagerServer.instance().getGroups().values())
                jsonArray.add(group.serialize());
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
