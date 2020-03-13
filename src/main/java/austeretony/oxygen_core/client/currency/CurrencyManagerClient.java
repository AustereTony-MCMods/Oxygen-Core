package austeretony.oxygen_core.client.currency;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import austeretony.oxygen_core.client.api.ClientReference;
import austeretony.oxygen_core.client.api.WatcherHelperClient;
import austeretony.oxygen_core.common.api.OxygenHelperCommon;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.common.util.JsonUtils;
import austeretony.oxygen_core.common.watcher.WatchedValue;
import net.minecraft.client.renderer.texture.DynamicTexture;

public class CurrencyManagerClient {

    private CurrencyProperties commonProperties;

    private final Map<Integer, CurrencyProperties> properties = new HashMap<>(3);

    public void registerCurrencyProperties(CurrencyProperties properties) {
        if (properties.getIndex() == OxygenMain.COMMON_CURRENCY_INDEX)
            this.commonProperties = properties;    
        this.properties.put(properties.getIndex(), properties);

        WatcherHelperClient.registerValue(new WatchedValue(properties.getIndex(), Long.BYTES));
    }

    public Collection<CurrencyProperties> getProperties() {
        return this.properties.values();
    }

    public CurrencyProperties getCommonCurrencyProperties() {
        return this.commonProperties;
    }

    public CurrencyProperties getProperties(int index) {
        if (index == OxygenMain.COMMON_CURRENCY_INDEX)
            return this.commonProperties;
        else
            return this.properties.get(index);
    }

    public void loadProperties() {
        for (CurrencyProperties properties : this.properties.values()) {
            this.loadData(properties);
            this.loadIcon(properties);
        }
    }

    private void loadData(CurrencyProperties properties) {
        String pathStr = OxygenHelperCommon.getConfigFolder() + "data/client/properties/currency/currency_" + properties.getIndex() + ".json";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try {
                properties.deserialize(JsonUtils.getExternalJsonData(pathStr).getAsJsonObject());
                OxygenMain.LOGGER.info("[Core] Loaded currency properties with index: <{}>.", properties.getIndex());
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] Failed to load currency properties with index: <{}>.", properties.getIndex());
                exception.printStackTrace();
            }
        } else {
            try {               
                Files.createDirectories(path.getParent());                                
                JsonUtils.createExternalJsonFile(pathStr, properties.serialize());
                OxygenMain.LOGGER.info("[Core] Created currency properties file with index: <{}>.", properties.getIndex());
            } catch (IOException exception) {   
                OxygenMain.LOGGER.error("[Core] Failed to create currency properties file with index: <{}>.", properties.getIndex());
                exception.printStackTrace();
            }     
        }
    }

    private void loadIcon(CurrencyProperties properties) {
        String pathStr = OxygenHelperCommon.getConfigFolder() + "data/client/properties/currency/currency_" + properties.getIndex() + ".png";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try {
                properties.setIcon(ClientReference.getMinecraft().getTextureManager().getDynamicTextureLocation("currency_" + properties.getIndex(), 
                        new DynamicTexture(ImageIO.read(new File(pathStr)))));
                OxygenMain.LOGGER.info("[Core] Loaded currency icon with index: <{}>.", properties.getIndex());
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] Failed to load currency icon with index: <{}>.", properties.getIndex());
                exception.printStackTrace();
            }        
        } else {
            try {               
                Files.createDirectories(path.getParent());     
                BufferedImage icon = null;
                try {
                    icon = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("assets/" + properties.getIcon().getResourceDomain() + "/" + properties.getIcon().getResourcePath()));
                } catch (IOException exception) {
                    exception.printStackTrace();
                }
                if (icon != null) {
                    ImageIO.write(icon, "png", path.toFile());
                    OxygenMain.LOGGER.info("[Core] Created currency icon with index: <{}>.", properties.getIndex());
                } else
                    OxygenMain.LOGGER.error("[Core] Failed to load currency icon with index: <{}>. External icon creation failed.", properties.getIndex());
            } catch (IOException exception) {   
                OxygenMain.LOGGER.error("[Core] Failed to create currency icon with index: <{}>.", properties.getIndex());
                exception.printStackTrace();
            }     
        }
    }
}
