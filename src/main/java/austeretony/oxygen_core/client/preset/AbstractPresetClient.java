package austeretony.oxygen_core.client.preset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import io.netty.buffer.ByteBuf;

public abstract class AbstractPresetClient implements PresetClient {

    private long versionId;

    @Override
    public long getVersionId() {
        return this.versionId;
    }

    @Override
    public boolean loadVersionId(String folder) {
        this.versionId = 0L;
        String pathStr = folder + this.getName() + "_version.txt";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathStr))) {  
                this.versionId = Long.parseLong(bufferedReader.readLine());
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
            }           
        }
        return false;
    }

    @Override
    public boolean save(String folder) {
        boolean successful = this.saveToFolder(folder);
        if (successful)
            return this.saveVersionId(folder);
        return false;
    }

    public abstract boolean saveToFolder(String folder);

    private boolean saveVersionId(String folder) {
        String idPathStr = folder + this.getName() + "_version.txt";
        Path idPath = Paths.get(idPathStr);
        try {               
            Files.createDirectories(idPath.getParent());             
            try (PrintStream printStream = new PrintStream(new File(idPathStr))) {
                printStream.println(this.versionId);
            } 
            return true;
        } catch (IOException exception) {      
            exception.printStackTrace();
        }
        return false;
    }

    @Override
    public void read(ByteBuf buffer) {
        this.reset();
        this.versionId = buffer.readLong();
        this.readFromBuf(buffer);
    }

    public abstract void reset();

    public abstract void readFromBuf(ByteBuf buffer);
}
