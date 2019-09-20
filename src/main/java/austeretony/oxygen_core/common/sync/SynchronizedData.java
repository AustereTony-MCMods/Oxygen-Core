package austeretony.oxygen_core.common.sync;

import io.netty.buffer.ByteBuf;

public interface SynchronizedData {

    long getId();

    void write(ByteBuf buffer);

    void read(ByteBuf buffer);
}