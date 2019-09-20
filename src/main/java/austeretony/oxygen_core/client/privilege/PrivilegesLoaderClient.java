package austeretony.oxygen_core.client.privilege;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen_core.client.OxygenManagerClient;
import austeretony.oxygen_core.client.api.OxygenHelperClient;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.privilege.EnumPrivilegeFileKey;
import austeretony.oxygen_core.common.privilege.Privilege;
import austeretony.oxygen_core.common.privilege.PrivilegedGroup;
import austeretony.oxygen_core.common.privilege.PrivilegedGroupImpl;
import austeretony.oxygen_core.common.util.JsonUtils;

public class PrivilegesLoaderClient {

    public static void loadPrivilegeDataAsync() {
        OxygenHelperClient.addIOTask(()->loadPrivilegedGroup());
    }

    private static void loadPrivilegedGroup() {
        String folder = OxygenHelperClient.getDataFolder() + "/client/players/" + OxygenHelperClient.getPlayerUUID() + "/privileges/group.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonObject groupObject = JsonUtils.getExternalJsonData(folder).getAsJsonObject();
                long groupId = groupObject.get(EnumPrivilegeFileKey.ID.name).getAsLong();
                if (groupId == OxygenManagerClient.instance().getPrivilegesManager().getGroupId()) {
                    OxygenManagerClient.instance().getPrivilegesManager().setPrivelegedGroup(PrivilegedGroupImpl.deserializeClient(groupObject));
                } else {
                    OxygenMain.LOGGER.info("Client privileged group id mismatch with id recieved from server.");
                    OxygenManagerClient.instance().getPrivilegesManager().requestGroupSync();
                }
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("Privileged group loading failed.");
                exception.printStackTrace();
            }       
        } else {            
            OxygenMain.LOGGER.info("Privileged group data file not exist.");
            OxygenManagerClient.instance().getPrivilegesManager().requestGroupSync();
        }
    }

    public static void savePrivilegedGroupAsync() {
        OxygenHelperClient.addIOTask(()->savePrivilegedGroup());
    }

    public static void savePrivilegedGroup() {
        String folder =  OxygenHelperClient.getDataFolder() + "/client/players/" + OxygenHelperClient.getPlayerUUID() + "/privileges/group.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            PrivilegedGroup group = OxygenManagerClient.instance().getPrivilegesManager().getPrivilegedGroup();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(EnumPrivilegeFileKey.ID.name, new JsonPrimitive(group.getId()));
            jsonObject.add(EnumPrivilegeFileKey.NAME.name, new JsonPrimitive(group.getName()));
            JsonArray privilegesArray = new JsonArray();
            for (Privilege privilege : group.getPrivileges())
                privilegesArray.add(privilege.serialize());
            jsonObject.add(EnumPrivilegeFileKey.PRIVILEGES.name, privilegesArray);
            JsonUtils.createExternalJsonFile(folder, jsonObject);
            OxygenMain.LOGGER.info("Saved privileged group.");
        } catch (IOException exception) {
            OxygenMain.LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
