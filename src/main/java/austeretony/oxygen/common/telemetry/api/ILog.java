package austeretony.oxygen.common.telemetry.api;

import java.io.BufferedOutputStream;
import java.io.IOException;

public interface ILog {

    byte getActivityIndex();

    long getTime();

    void write(BufferedOutputStream bos) throws IOException;
}
