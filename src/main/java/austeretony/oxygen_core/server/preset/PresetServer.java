package austeretony.oxygen_core.server.preset;

import io.netty.buffer.ByteBuf;

public interface PresetServer {

    int getId();

    String getDomain();

    String getDisplayName();

    long getVersionId();

    boolean load(String folder);

    void write(ByteBuf buffer);
}