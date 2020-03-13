package austeretony.oxygen_core.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import austeretony.oxygen_core.common.api.CommonReference;
import austeretony.oxygen_core.common.main.OxygenMain;
import austeretony.oxygen_core.server.api.TimeHelperServer;

public class ServerData {

    public String worldFolder;

    private String dataFolder;

    private long worldId;

    public String getDataFolder() {
        return this.dataFolder;
    }

    public long getWorldId() {
        return this.worldId;
    }

    public void createOrLoadWorldId(String worldFolder) {
        this.worldFolder = worldFolder;
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
                OxygenMain.LOGGER.info("[Core] Loaded world id: {}", worldIdStr);
            } catch (IOException exception) {
                OxygenMain.LOGGER.error("[Core] World id loading failed.");
                exception.printStackTrace();
            }           
        } else {
            this.worldId = Long.parseLong(OxygenMain.ID_DATE_FORMAT.format(TimeHelperServer.getZonedDateTime()));
            worldIdStr = String.valueOf(this.worldId);
            OxygenMain.LOGGER.info("[Core] Created world id: {}", worldIdStr);
            try {               
                Files.createDirectories(worldIdPath.getParent());             
                try (PrintStream printStream = new PrintStream(new File(worldIdFilePathStr))) {
                    printStream.println(worldIdStr);
                } 
            } catch (IOException exception) {      
                OxygenMain.LOGGER.error("[Core] World id saving failed.");
                exception.printStackTrace();
            }
        }
        this.dataFolder = CommonReference.getGameFolder() + "/oxygen/worlds/" + this.worldId;
    }
}
