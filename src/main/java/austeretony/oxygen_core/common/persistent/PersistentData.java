package austeretony.oxygen_core.common.persistent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;

public interface PersistentData {

    String getDisplayName();

    String getPath();

    boolean isChanged();

    void setChanged(boolean flag);

    void write(BufferedOutputStream bos) throws IOException;

    void read(BufferedInputStream bis) throws IOException;

    void reset();
}
