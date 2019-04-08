package austeretony.oxygen.common.io;

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

import austeretony.oxygen.common.api.IOxygenTask;
import austeretony.oxygen.common.api.OxygenHelperServer;
import austeretony.oxygen.common.main.OxygenMain;
import austeretony.oxygen.common.main.OxygenManagerServer;
import austeretony.oxygen.common.reference.CommonReference;

public class OxygenIOServer {

    public final String worldFolder;

    public final int maxPlayers;

    private volatile String dataFolder;

    private volatile long worldId;

    private OxygenIOServer(String worldFolder, int maxPlayers) {
        this.worldFolder = worldFolder;
        this.maxPlayers = maxPlayers;
        this.createOrLoadWorldIdDelegated();
    }

    public static OxygenIOServer create(String worldFolder, int maxPlayers) {
        return new OxygenIOServer(worldFolder, maxPlayers);
    }

    public static OxygenIOServer instance() {
        return OxygenManagerServer.instance().getIO();
    }

    private void createOrLoadWorldIdDelegated() {
        OxygenHelperServer.addIOTaskServer(new IOxygenTask() {

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

    public static String getDataFolder() {
        return instance().dataFolder;
    }

    public static long getWorldId() {
        return instance().worldId;
    }

    public static int getMaxPlayers() {
        return instance().maxPlayers;
    }
}
