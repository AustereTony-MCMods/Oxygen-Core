package austeretony.oxygen_core.client.gui.settings;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.JsonUtils;

public class GUISettingsLoader {

    private final GUISettings settings;

    public GUISettingsLoader(GUISettings settings) {
        this.settings = settings;
    }

    public void loadSettings() {
        String 
        internalPathStr = "assets/oxygen_core/gui_settings.json",
        externalPathStr = OxygenHelperCommon.getConfigFolder() + "data/client/gui_settings.json";
        Path externalPath =  Paths.get(externalPathStr);
        if (Files.exists(externalPath)) {
            try {      
                this.loadData(JsonUtils.getExternalJsonData(externalPathStr).getAsJsonObject());
            } catch (IOException | NullPointerException exception) {  
                OxygenMain.LOGGER.error("External GUI settings file damaged or outdated! Creating default config...");
                try {
                    this.createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalPathStr).getAsJsonObject(), externalPathStr);
                } catch (IOException e) {
                    OxygenMain.LOGGER.error("Internal GUI settings file damaged!");
                    e.printStackTrace();
                }  
            }       
        } else {                
            try {               
                Files.createDirectories(externalPath.getParent());                                
                this.createExternalCopyAndLoad(JsonUtils.getInternalJsonData(internalPathStr).getAsJsonObject(), externalPathStr);  
            } catch (IOException exception) {   
                OxygenMain.LOGGER.error("Internal GUI settings file damaged!");
                exception.printStackTrace();
            }     
        }      
    }

    private void createExternalCopyAndLoad(JsonObject internalFile, String externalFile) {       
        try {           
            JsonUtils.createExternalJsonFile(externalFile, internalFile);            
        } catch (IOException exception) {       
            OxygenMain.LOGGER.error("External GUI settings file creation failed.");
            exception.printStackTrace();
        }
        this.loadData(internalFile);
    }

    private void loadData(JsonObject file) {
        String currentProfileName = file.get(EnumSettingsFileKey.SETTINGS_PROFILE.key).getAsString();
        for (JsonElement element : file.get(EnumSettingsFileKey.PROFILES.key).getAsJsonArray())
            this.settings.addProfile(GUISettingsProfile.deserialize(element.getAsJsonObject()));
        this.settings.setCurrentProfile(this.settings.getProfile(currentProfileName));
        OxygenMain.LOGGER.info("GUI settings loaded. Set profile to: {}.", currentProfileName);
    }
}
