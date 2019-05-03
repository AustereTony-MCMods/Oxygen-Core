package austeretony.oxygen.common.api;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface IPersistentData {

    String getName();
    
    String getModId();

    String getPath();

    void write(BufferedOutputStream bos) throws IOException;

    void read(BufferedInputStream bis) throws IOException;
}
