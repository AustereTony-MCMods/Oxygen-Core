package austeretony.oxygen.common.io;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import austeretony.oxygen.common.config.OxygenConfig;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.reference.CommonReference;
import austeretony.oxygen.common.util.JsonUtils;

public class OxygenIOClient {

    public static void loadCustomLocalization(List<String> languageList, Map<String, String> properties) {
        if (OxygenConfig.CUSTOM_LOCALIZATION.getBooleanValue()) {
            String localizationFolder = CommonReference.getGameFolder() + "/config/oxygen/localization/localization.json";
            Path localizationPath = Paths.get(localizationFolder);      
            if (Files.exists(localizationPath)) {
                try {       
                    loadLocalization(JsonUtils.getExternalJsonData(localizationFolder).getAsJsonObject(), languageList, properties);
                } catch (IOException exception) {       
                    exception.printStackTrace();
                    return;
                }
            } else {
                try {               
                    Files.createDirectories(localizationPath.getParent());
                    JsonObject loclization = JsonUtils.getInternalJsonData("assets/oxygen/localization.json").getAsJsonObject();
                    JsonUtils.createExternalJsonFile(localizationFolder, loclization);    
                    loadLocalization(loclization, languageList, properties);
                } catch (IOException exception) {               
                    exception.printStackTrace();
                }   
            }
        }
    }

    private static void loadLocalization(JsonObject localizationFile, List<String> languageList, Map<String, String> properties) {
        OxygenMain.OXYGEN_LOGGER.info("Searching for custom localization...");
        for (String lang : languageList) {
            JsonElement entriesElement = localizationFile.get(lang.toLowerCase());
            if (entriesElement != null) {
                OxygenMain.OXYGEN_LOGGER.info("Loading custom <{}> localization...", lang);
                JsonObject localizationObject = entriesElement.getAsJsonObject();
                Set<Map.Entry<String, JsonElement>> entrySet = localizationObject.entrySet();
                for (Map.Entry<String, JsonElement> entry : entrySet)
                    properties.put(entry.getKey(), entry.getValue().getAsJsonObject().getAsString());
            } else {
                OxygenMain.OXYGEN_LOGGER.error("Custom localization for <{}> undefined!", lang);
            }
        }
    }
}
