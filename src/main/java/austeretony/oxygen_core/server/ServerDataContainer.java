package austeretony.oxygen_core.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;

public class ServerDataContainer {

    public String worldFolder;

    public int maxPlayers;

    private String dataFolder;

    private long worldId;

    public String getDataFolder() {
        return this.dataFolder;
    }

    public long getWorldId() {
        return this.worldId;
    }
    
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
                OxygenMain.LOGGER.info("Loaded world id: {}.", worldIdStr);
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("World id loading failed.");
                exception.printStackTrace();
            }           
        } else {
            this.worldId = Long.parseLong(new SimpleDateFormat("yyMMddHHmmss").format(new Date()));
            worldIdStr = String.valueOf(this.worldId);
            OxygenMain.LOGGER.info("Created world id: {}.", worldIdStr);
            try {               
                Files.createDirectories(worldIdPath.getParent());             
                try (PrintStream printStream = new PrintStream(new File(worldIdFilePathStr))) {
                    printStream.println(worldIdStr);
                } 
            } catch (IOException exception) {      
                OxygenMain.LOGGER.error("World id saving failed.");
                exception.printStackTrace();
            }
        }
        this.dataFolder = CommonReference.getGameFolder() + "/oxygen/worlds/" + this.worldId;
    }
}