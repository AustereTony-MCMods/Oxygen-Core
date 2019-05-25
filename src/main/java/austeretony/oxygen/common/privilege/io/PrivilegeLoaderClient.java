package austeretony.oxygen.common.privilege.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.client.OxygenManagerClient;
import austeretony.oxygen.client.api.OxygenHelperClient;
import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.util.JsonUtils;
import austeretony.oxygen.common.util.OxygenUtils;

public class PrivilegeLoaderClient {

    private final OxygenManagerClient manager;

    public PrivilegeLoaderClient(OxygenManagerClient manager) {
        this.manager = manager;
    }

    public void loadPrivilegeDataDelegated() {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadPrivilegedGroup();
            }           
        });
    }

    private void loadPrivilegedGroup() {
        String folder = OxygenManagerClient.instance().getDataFolder() + "/client/players/" + OxygenManagerClient.instance().getPlayerUUID() + "/privilege/group.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonObject groupObject = JsonUtils.getExternalJsonData(folder).getAsJsonObject();
                long groupId = groupObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.ID)).getAsLong();
                if (groupId == OxygenManagerClient.instance().getGroupId()) {
                    this.manager.getPrivilegeManager().setPrivelegedGroup(PrivilegedGroup.deserializeClient(groupObject));
                } else {
                    OxygenMain.OXYGEN_LOGGER.info("Client group id mismatch with id recieved from server.");
                    this.manager.getPrivilegeManager().requestGroupSync();
                }
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Privileged group loading failed.");
                exception.printStackTrace();
            }       
        } else {            
            OxygenMain.OXYGEN_LOGGER.info("Group data file not exist.");
            this.manager.getPrivilegeManager().requestGroupSync();
        }
    }

    public void savePrivilegedGroupDelegated() {
        OxygenHelperClient.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePrivilegedGroup();
            }           
        });
    }

    public void savePrivilegedGroup() {
        String folder = OxygenManagerClient.instance().getDataFolder() + "/client/players/" + OxygenManagerClient.instance().getPlayerUUID() + "/privilege/group.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            IPrivilegedGroup group = this.manager.getPrivilegeManager().getPrivilegedGroup();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.ID), new JsonPrimitive(group.getId()));
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.NAME), new JsonPrimitive(group.getName()));
            JsonArray privilegesArray = new JsonArray();
            for (IPrivilege privilege : group.getPrivileges())
                privilegesArray.add(privilege.serialize());
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PRIVILEGES), privilegesArray);
            JsonUtils.createExternalJsonFile(folder, jsonObject);
            OxygenMain.PRIVILEGE_LOGGER.info("Saved privileged group.");
        } catch (IOException exception) {
            OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
