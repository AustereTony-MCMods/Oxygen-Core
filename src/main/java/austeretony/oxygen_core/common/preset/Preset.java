package austeretony.oxygen_core.common.preset;

import io.netty.buffer.ByteBuf;

public interface Preset {

    int getId();

    String getName();

    long getVersion();

    void loadVersion(String worldId);

    void load(String worldId);

    void save();

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}
