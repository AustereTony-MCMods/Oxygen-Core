package austeretony.oxygen.common;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import austeretony.oxygen.common.api.IPersistentData;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.entity.player.EntityPlayer;

public class OxygenLoaderServer {

    public String worldFolder;

    public int maxPlayers;

    private String dataFolder;

    private long worldId;

    public void createOrLoadWorldId(String worldFolder, int maxPlayers) {
        this.worldFolder = worldFolder;
        this.maxPlayers = maxPlayers;
        this.createOrLoadWorldId();
    }

    private void createOrLoadWorldId() {
        String 
        worldIdFilePathStr = this.worldFolder + "/oxygen/worldid.txt",
        worldIdStr;
        Path worldIdPath = Paths.get(worldIdFilePathStr);
        if (Files.exists(worldIdPath)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(worldIdFilePathStr))) {  
                worldIdStr = bufferedReader.readLine();
                this.worldId = Long.parseLong(worldIdStr);
                OxygenMain.OXYGEN_LOGGER.info("Loaded world id: {}.", worldIdStr);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("World id loading failed.");
                exception.printStackTrace();
            }           
        } else {
            this.worldId = Long.parseLong(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
            worldIdStr = String.valueOf(this.worldId);
            OxygenMain.OXYGEN_LOGGER.info("Created world id: {}.", worldIdStr);
            try {               
                Files.createDirectories(worldIdPath.getParent());             
                try (PrintStream printStream = new PrintStream(new File(worldIdFilePathStr))) {
                    printStream.println(worldIdStr);
                } 
            } catch (IOException exception) {      
                OxygenMain.OXYGEN_LOGGER.error("World id saving failed.");
                exception.printStackTrace();
            }
        }
        this.dataFolder = CommonReference.getGameFolder() + "/oxygen/worlds/" + this.worldId;
    }

    public static void loadPlayerDataCreateSharedEntryDelegated(UUID playerUUID, IPersistentData persistentData, EntityPlayer player) {
        OxygenHelperServer.addCommonIOTask(()->{                
            loadPersistentData(persistentData);              
            OxygenManagerServer.instance().getSharedDataManager().createPlayerSharedDataEntrySynced(player);
            WatcherManagerServer.instance().initWatcher(player, playerUUID);
        });
    }

    public static void loadPersistentDataDelegated(IPersistentData persistentData) {
        OxygenHelperServer.addCommonIOTask(()->loadPersistentData(persistentData));
    }

    public static void loadServiceDataDelegated(IPersistentData persistentData) {
        OxygenHelperServer.addServiceIOTask(()->loadPersistentData(persistentData));
    }

    public static void loadPersistentData(IPersistentData persistentData) {
        String folder = OxygenHelperServer.getDataFolder() + "/server/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(folder))) {    
                persistentData.read(bis);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Server persistent data <{}> loading failed for <{}>, path: {}", persistentData.getName(), persistentData.getModId(), persistentData.getPath());
                exception.printStackTrace();
            }
        }
    }

    public static void savePersistentDataDelegated(IPersistentData persistentData) {
        OxygenHelperServer.addCommonIOTask(()->savePersistentData(persistentData));
    }

    public static void saveServiceDataDelegated(IPersistentData persistentData) {
        OxygenHelperServer.addServiceIOTask(()->savePersistentData(persistentData));
    }

    public static void savePersistentData(IPersistentData persistentData) {
        String folder = OxygenHelperServer.getDataFolder() + "/server/" + persistentData.getPath();
        Path path = Paths.get(folder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(folder))) {   
            persistentData.write(bos);
        } catch (IOException exception) {
            OxygenMain.OXYGEN_LOGGER.error("Server persistent data <{}> saving failed for <{}>, path: {}", persistentData.getName(), persistentData.getModId(), persistentData.getPath());
            exception.printStackTrace();
        }
    }

    public String getDataFolder() {
        return this.dataFolder;
    }

    public int getMaxPlayers() {
        return this.maxPlayers;
    }

    public long getWorldId() {
        return this.worldId;
    }
}
