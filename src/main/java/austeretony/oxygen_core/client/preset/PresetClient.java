package austeretony.oxygen_core.client.preset;

import io.netty.buffer.ByteBuf;

public interface PresetClient {

    int getId();

    String getDomain();

    String getDisplayName();

    long getVersionId();

    boolean loadVersionId(String folder);

    boolean load(String folder);

    boolean save(String folder);

    boolean reloadAfterSave();

    void read(ByteBuf buffer);
}