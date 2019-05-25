package austeretony.oxygen.common.util;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.text.TextFormatting;

public class OxygenUtils {

    public static final DateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    private static final Set<String> OUTDATED_DATA = new HashSet<String>();

    public static long createDataStampedId() {
        return Long.parseLong(SIMPLE_ID_DATE_FORMAT.format(new Date()));
    }

    public static void removePreviousData(String modId, boolean flag) {
        if (flag)               
            OUTDATED_DATA.add(modId);
    }

    public static String keyFromEnum(Enum enumKey) {
        return enumKey.toString().toLowerCase();
    }

    public static String formattingCode(TextFormatting enumFormatting) {
        return enumFormatting.getFriendlyName();
    }

    public static TextFormatting formattingFromCode(String code) {
        return TextFormatting.getValueByName(code);
    }

    public static JsonObject updateConfig(JsonObject internalConfig, String externalConfigFolder, IConfigHolder configHolder) throws IOException {
        try {            
            JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
            externalConfigOld = JsonUtils.getExternalJsonData(externalConfigFolder).getAsJsonObject();   
            JsonElement versionElement = externalConfigOld.get("version");
            boolean outdated = isOutdated(versionElement.getAsString(), configHolder.getVersion());
            if (versionElement == null || outdated) {
                if (outdated) {
                    File oxygenFolder = new File(CommonReference.getGameFolder() + "/oxygen");
                    if (oxygenFolder.exists()) {
                        OxygenMain.OXYGEN_LOGGER.info("Removing outdated Oxygen data...");
                        removeOutdatedData(oxygenFolder);
                    }
                }
                OxygenMain.OXYGEN_LOGGER.info("Updating <{}> config file...", configHolder.getModId());
                externalConfigNew = new JsonObject();
                externalConfigNew.add("version", new JsonPrimitive(configHolder.getVersion()));
                Map<String, JsonElement> 
                internalData = new LinkedHashMap<String, JsonElement>(),
                externlDataOld = new HashMap<String, JsonElement>(),
                internalGroup, externlGroupOld;
                for (Map.Entry<String, JsonElement> entry : internalConfig.entrySet())
                    internalData.put(entry.getKey(), entry.getValue());
                for (Map.Entry<String, JsonElement> entry : externalConfigOld.entrySet())
                    externlDataOld.put(entry.getKey(), entry.getValue());      
                for (String key : internalData.keySet()) {
                    internalGroup = new LinkedHashMap<String, JsonElement>();
                    externlGroupOld = new HashMap<String, JsonElement>();
                    externalGroupNew = new JsonObject();
                    for (Map.Entry<String, JsonElement> entry : internalData.get(key).getAsJsonObject().entrySet())
                        internalGroup.put(entry.getKey(), entry.getValue());
                    if (externlDataOld.containsKey(key)) {                    
                        for (Map.Entry<String, JsonElement> entry : externlDataOld.get(key).getAsJsonObject().entrySet())
                            externlGroupOld.put(entry.getKey(), entry.getValue());   
                        for (String k : internalGroup.keySet()) {
                            if (externlGroupOld.containsKey(k))
                                externalGroupNew.add(k, externlGroupOld.get(k));
                            else 
                                externalGroupNew.add(k, internalGroup.get(k));
                        }
                    } else {
                        for (String k : internalGroup.keySet())
                            externalGroupNew.add(k, internalGroup.get(k));
                    }
                    externalConfigNew.add(key, externalGroupNew);
                    JsonUtils.createExternalJsonFile(externalConfigFolder, externalConfigNew);
                }
                return externalConfigNew;
            }
            return externalConfigOld;            
        } catch (IOException exception) {  
            OxygenMain.OXYGEN_LOGGER.error("External configuration file for <{}> damaged!", configHolder.getModId());
            exception.printStackTrace();
        }
        return null;
    }

    public static boolean isOutdated(String currentVersion, String availableVersion) {        
        try {
            String[] 
                    cSplitted = currentVersion.split("[:]"),
                    aSplitted = availableVersion.split("[:]");    
            String 
            cVer = cSplitted[0],
            cType = cSplitted[1],
            cRev = cSplitted[2],
            aVer = aSplitted[0],
            aType = aSplitted[1],
            aRev = aSplitted[2];
            String[]
                    cVerSplitted = cVer.split("[.]"),
                    aVerSplitted = aVer.split("[.]");
            int verDiff, revDiff;               
            for (int i = 0; i < 3; i++) {                                                             
                verDiff = Integer.parseInt(aVerSplitted[i]) - Integer.parseInt(cVerSplitted[i]);                                                                                           
                if (verDiff > 0)
                    return true;                                
                if (verDiff < 0)
                    return false;
            }  
            if (aType.equals("release") && (cType.equals("beta") || cType.equals("alpha")))
                return true;
            if (aType.equals("beta") && cType.equals("alpha"))
                return true;
            revDiff = Integer.parseInt(aRev) - Integer.parseInt(cRev);                                                                                           
            if (revDiff > 0)
                return true;                                
            if (revDiff < 0)
                return false;
            return false;
        } catch (Exception exception) { 
            OxygenMain.OXYGEN_LOGGER.error("Versions comparison failed!");               
            exception.printStackTrace();
        }
        return true;
    }

    public static void removeOutdatedData(File folder) {
        File[] folderEntries = folder.listFiles();
        for (File entry : folderEntries)  {
            if (entry.isDirectory()) {
                if (OUTDATED_DATA.contains(entry.getName()))    
                    for (File file : entry.listFiles())
                        file.delete();                    
                removeOutdatedData(entry);
                continue;
            }
        }
    }
}
