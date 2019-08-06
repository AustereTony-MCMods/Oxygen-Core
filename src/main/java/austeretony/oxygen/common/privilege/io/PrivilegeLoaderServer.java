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
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.api.event.OxygenPrivilegesLoadedEvent;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.util.JsonUtils;
import austeretony.oxygen.util.OxygenUtils;
import net.minecraftforge.common.MinecraftForge;

public class PrivilegeLoaderServer {

    public static void loadPrivilegeDataDelegated() {
        OxygenHelperServer.addServiceIOTask(()->{
            loadPrivilegedGroups();
            loadPlayerList();
            OxygenManagerServer.instance().getPrivilegeManager().addDefaultGroups();

            CommonReference.delegateToServerThread(()->MinecraftForge.EVENT_BUS.post(new OxygenPrivilegesLoadedEvent()));
        });
    }

    private static void loadPlayerList() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/players.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray jsonArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                JsonObject object;
                UUID playerUUID;
                for (JsonElement element : jsonArray) {
                    object = element.getAsJsonObject();
                    playerUUID = UUID.fromString(object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PLAYER_UUID)).getAsString());
                    OxygenManagerServer.instance().getPrivilegeManager().promotePlayer(playerUUID, object.get(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.GROUP)).getAsString());
                }
                OxygenMain.OXYGEN_LOGGER.info("Loaded privileged players list.");
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Privileged players list loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    private static void loadPrivilegedGroups() {
        String folder = OxygenHelperServer.getDataFolder() + "/server/privilege/groups.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonArray groupArray = JsonUtils.getExternalJsonData(folder).getAsJsonArray();
                for (JsonElement groupElement : groupArray)
                    OxygenManagerServer.instance().getPrivilegeManager().addGroup(PrivilegedGroup.deserializeServer(groupElement.getAsJsonObject()), false);
                OxygenMain.OXYGEN_LOGGER.info("Privileged groups loaded.");
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Privileged groups loading failed.");
                exception.printStackTrace();
            }       
        }
    }

    public static void savePlayerListDelegated() {
        OxygenHelperServer.addServiceIOTask(()->savePlayerList());
    }

    public static void savePlayerList() {
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
            for (Map.Entry<UUID, String> entry : OxygenManagerServer.instance().getPrivilegeManager().getPlayers().entrySet()) {
                object = new JsonObject();
                object.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.PLAYER_UUID), new JsonPrimitive(entry.getKey().toString()));
                object.add(OxygenUtils.keyFromEnum(EnumPrivilegeFileKey.GROUP), new JsonPrimitive(entry.getValue()));
                jsonArray.add(object);
            }
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.OXYGEN_LOGGER.error("Privileged players list saving failed.");
            exception.printStackTrace();
        }       
    }

    public static void savePrivilegedGroupsDelegated() {
        OxygenHelperServer.addServiceIOTask(()->savePrivilegedGroups());
    }

    public static void savePrivilegedGroups() {
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
            for (IPrivilegedGroup group : OxygenManagerServer.instance().getPrivilegeManager().getGroups().values())
                jsonArray.add(group.serialize());
            JsonUtils.createExternalJsonFile(folder, jsonArray);
        } catch (IOException exception) {
            OxygenMain.OXYGEN_LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
