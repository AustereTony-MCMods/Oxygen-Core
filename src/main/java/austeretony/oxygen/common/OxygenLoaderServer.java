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

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.core.api.CommonReference;
import austeretony.oxygen.common.main.OxygenMain;
import net.minecraft.entity.player.EntityPlayerMP;

public class OxygenLoaderServer {

    private final OxygenManagerServer manager;

    public String worldFolder;

    public int maxPlayers;

    private String dataFolder;

    private long worldId;

    public OxygenLoaderServer(OxygenManagerServer manager) {
        this.manager = manager;
    }

    public void createOrLoadWorldIdDelegated(String worldFolder, int maxPlayers) {
        this.worldFolder = worldFolder;
        this.maxPlayers = maxPlayers;
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                createOrLoadWorldId();
            }           
        });
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

    public void loadPlayerDataDelegated(UUID playerUUID, EntityPlayerMP playerMP) {
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                loadPlayerData(playerUUID);
                manager.createSharedData(playerUUID, playerMP);
            }     
        });
    }

    public void loadPlayerData(UUID playerUUID) {
        String folder = OxygenHelperServer.getDataFolder() + "/server/players/" + playerUUID + "/oxygen/profile.dat";
        Path path = Paths.get(folder);
        if (Files.exists(path)) {
            try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(folder))) {    
                this.manager.getPlayerData(playerUUID).read(bis);
                //OxygenMain.OXYGEN_LOGGER.info("Player {} server data loaded.", playerUUID);
            } catch (IOException exception) {
                OxygenMain.OXYGEN_LOGGER.error("Player {} server data loading failed.", playerUUID);
                exception.printStackTrace();
            }
        }
    }

    public void savePlayerDataDelegated(UUID playerUUID) {
        OxygenHelperServer.addIOTask(new IOxygenTask() {

            @Override
            public void execute() {
                savePlayerData(playerUUID);
            }     
        });
    }

    public void savePlayerData(UUID playerUUID) {
        String folder = OxygenHelperServer.getDataFolder() + "/server/players/" + playerUUID + "/oxygen/profile.dat";
        Path path = Paths.get(folder);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path.getParent());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(folder))) {   
            this.manager.getPlayerData(playerUUID).write(bos);
        } catch (IOException exception) {
            OxygenMain.OXYGEN_LOGGER.error("Player {} server data saving failed.", playerUUID);
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
