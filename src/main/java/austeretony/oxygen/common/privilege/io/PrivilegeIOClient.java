package austeretony.oxygen.common.privilege.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.api.OxygenHelperClient;
import austeretony.oxygen.common.api.OxygenTask;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerClient;
import austeretony.oxygen.common.privilege.IPrivilege;
import austeretony.oxygen.common.privilege.IPrivilegedGroup;
import austeretony.oxygen.common.privilege.PrivilegeManagerClient;
import austeretony.oxygen.common.privilege.api.PrivilegedGroup;
import austeretony.oxygen.common.util.JsonUtils;
import austeretony.oxygen.common.util.OxygenUtils;

public class PrivilegeIOClient {

    private final String privilegeFolder;

    private PrivilegeIOClient() {
        this.privilegeFolder = OxygenManagerClient.instance().getDataFolder() + "/client/privilege";
        this.loadPrivilegeDataDelegated();
    }

    public static PrivilegeIOClient create() {
        return new PrivilegeIOClient();
    }

    public static PrivilegeIOClient instance() {
        return OxygenMain.getPrivilegeManagerClient().getIO();
    }

    public void loadPrivilegeDataDelegated() {
        OxygenHelperClient.addIOTaskClient(new OxygenTask() {

            @Override
            public void execute() {
                loadPrivilegedGroup();
            }           
        });
    }

    private void loadPrivilegedGroup() {
        String folder = this.privilegeFolder + "/group.json";
        Path path = Paths.get(folder);     
        if (Files.exists(path)) {
            try {      
                JsonObject groupObject = JsonUtils.getExternalJsonData(folder).getAsJsonObject();
                long groupId = groupObject.get(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.ID)).getAsLong();
                if (groupId == OxygenManagerClient.instance().getGroupId()) {
                    PrivilegeManagerClient.instance().setPrivelegedGroup(PrivilegedGroup.deserializeClient(groupObject));
                } else
                    PrivilegeManagerClient.instance().requestGroupSync();
            } catch (IOException exception) {
                OxygenMain.PRIVILEGE_LOGGER.error("Privileged group loading failed.");
                exception.printStackTrace();
            }       
        } else                    
            PrivilegeManagerClient.instance().requestGroupSync();
    }

    public void savePrivilegedGroupDelegated() {
        OxygenHelperClient.addIOTaskClient(new OxygenTask() {

            @Override
            public void execute() {
                savePrivilegedGroup();
            }           
        });
    }

    public void savePrivilegedGroup() {
        String folder = this.privilegeFolder + "/group.json";
        Path path = Paths.get(folder);    
        if (!Files.exists(path)) {
            try {                   
                Files.createDirectories(path.getParent());              
            } catch (IOException exception) {     
                exception.printStackTrace();
            }
        }
        try {      
            IPrivilegedGroup group = PrivilegeManagerClient.instance().getPrivilegedGroup();
            JsonObject jsonObject = new JsonObject();
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.ID), new JsonPrimitive(group.getGroupId()));
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.NAME), new JsonPrimitive(group.getGroupName()));
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.TITLE), new JsonPrimitive(group.getTitle()));
            JsonArray privilegesArray = new JsonArray();
            for (IPrivilege privilege : group.getPrivileges())
                privilegesArray.add(privilege.serealize());
            jsonObject.add(OxygenUtils.keyFromEnum(EnumPrivilegeFilesKeys.PRIVILEGES), privilegesArray);
            JsonUtils.createExternalJsonFile(folder, jsonObject);
            OxygenMain.PRIVILEGE_LOGGER.info("Saved privileged group.");
        } catch (IOException exception) {
            OxygenMain.PRIVILEGE_LOGGER.error("Privileged groups saving failed.");
            exception.printStackTrace();
        }       
    }
}
