package austeretony.oxygen_core.server.preset;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import austeretony.oxygen_core.common.main.OxygenMain;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public abstract class AbstractPresetServer implements PresetServer {

    private long versionId;

    private final ByteBuf compressed = Unpooled.buffer();

    @Override
    public long getVersionId() {
        return this.versionId;
    }

    @Override
    public boolean load(String folder) {
        if (!this.loadVersionId(folder))
            return false;
        boolean successful = this.loadFromFolder(folder);
        if (successful)
            this.compress();
        return successful;
    }

    public abstract boolean loadFromFolder(String folder);

    private boolean loadVersionId(String folder) {
        String pathStr = folder + this.getName() + "_version.txt";
        Path path = Paths.get(pathStr);
        if (Files.exists(path)) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathStr))) {  
                this.versionId = Long.parseLong(bufferedReader.readLine());
                return true;
            } catch (IOException exception) {
                exception.printStackTrace();
            }           
        } else {
            this.versionId = Long.parseLong(OxygenMain.ID_DATE_FORMAT.format(new Date()));
            try {               
                Files.createDirectories(path.getParent());             
                try (PrintStream printStream = new PrintStream(new File(pathStr))) {
                    printStream.println(this.versionId);
                } 
                return true;
            } catch (IOException exception) {      
                exception.printStackTrace();
            }
        }
        return false;
    }

    private void compress() {
        this.compressed.writeLong(this.versionId);
        this.writeToBuf(this.compressed);
    }

    public abstract void writeToBuf(ByteBuf buffer);

    @Override
    public void write(ByteBuf buffer) {
        buffer.writeBytes(this.compressed, 0, this.compressed.writerIndex());
    }
}
