package austeretony.oxygen.client.gui.settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.util.JsonUtils;

public class GUISettingsLoader {

    private final GUISettings settings;

    public GUISettingsLoader(GUISettings settings) {
        this.settings = settings;
    }

    public void loadSettings() {
        String 
        internalPathStr = "assets/oxygen/gui_settings.json",
        externalPathStr = CommonReference.getGameFolder() + "/oxygen/client/settings/gui/gui_setting.json";
        Path externalPath =  Paths.get(externalPathStr);
        if (Files.exists(externalPath)) {
            try {      
                this.loadData(JsonUtils.getExternalJsonData(externalPathStr).getAsJsonObject());
            } catch (IOException exception) {  
                OxygenMain.OXYGEN_LOGGER.error("External GUI settings file damaged!");
                exception.printStackTrace();
            }       
        } else {                
            try {               
                Files.createDirectories(externalPath.getParent());                                
                this.createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalPathStr).getAsJsonObject(), externalPathStr);  
            } catch (IOException exception) {   
                OxygenMain.OXYGEN_LOGGER.error("Internal GUI settings file damaged!");
                exception.printStackTrace();
            }     
        }      
    }

    private void createExternalCopyAndLoad(JsonObject internalFile, String externalFile) {       
        try {           
            JsonUtils.createExternalJsonFile(externalFile, internalFile);            
        } catch (IOException exception) {       
            OxygenMain.OXYGEN_LOGGER.error("External GUI settings file creation failed.");
            exception.printStackTrace();
        }
        this.loadData(internalFile);
    }

    private void loadData(JsonObject file) {
        String currentProfileName = file.get(EnumSettingKeys.SETTINGS_PROFILE.key).getAsString();
        for (JsonElement element : file.get(EnumSettingKeys.PROFILES.key).getAsJsonArray())
            this.settings.addProfile(GUISettingsProfile.deserialize(element.getAsJsonObject()));
        this.settings.setCurrentProfile(this.settings.getProfile(currentProfileName));
        OxygenMain.OXYGEN_LOGGER.info("GUI settings loaded. Set profile to: {}.", currentProfileName);
    }
}
