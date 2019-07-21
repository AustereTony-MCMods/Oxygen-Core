package austeretony.oxygen.util;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import austeretony.oxygen.client.core.api.ClientReference;
import austeretony.oxygen.common.config.IConfigHolder;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.util.text.TextFormatting;

public class OxygenUtils {

    public static final DateFormat SIMPLE_ID_DATE_FORMAT = new SimpleDateFormat("yyMMddHHmmssSSS");

    public static long createDataStampedId() {
        return Long.parseLong(SIMPLE_ID_DATE_FORMAT.format(new Date()));
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

    public static String getLastActivityTimeLocalizedString(long millis) {
        if (millis > 0L) {
            long 
            delta = System.currentTimeMillis() - millis,
            hours = delta / 3_600_000L,
            days;        
            if (hours < 24L) {
                if (hours % 10L == 1L)
                    return ClientReference.localize("oxygen.lastActivity.hour", hours);
                else
                    return ClientReference.localize("oxygen.lastActivity.hours", hours);
            } else {
                days = hours / 24L;
                if (days % 10L == 1L)
                    return ClientReference.localize("oxygen.lastActivity.day", days);
                else               
                    return ClientReference.localize("oxygen.lastActivity.days", days);
            }
        } else
            return ClientReference.localize("oxygen.lastActivity.noData");
    }

    public static JsonObject updateConfig(JsonObject internalConfig, String externalConfigFolder, IConfigHolder configHolder) throws IOException {
        try {            
            JsonObject externalConfigOld, externalConfigNew, externalGroupNew;
            externalConfigOld = JsonUtils.getExternalJsonData(externalConfigFolder).getAsJsonObject();   
            JsonElement versionElement = externalConfigOld.get("version");
            if (versionElement == null || isOutdated(versionElement.getAsString(), configHolder.getVersion())) {
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
}
